package org.example.minishop.service;

import jakarta.transaction.Transactional;
import org.example.minishop.dto.order.CreateOrderRequest;
import org.example.minishop.dto.order.OrderItemResponse;
import org.example.minishop.dto.order.OrderResponse;
import org.example.minishop.exception.order.InsufficientItemsInStock;
import org.example.minishop.exception.order.OrderAlreadyCancelledException;
import org.example.minishop.exception.order.OrderNotFoundException;
import org.example.minishop.exception.product.ProductNotFoundException;
import org.example.minishop.model.*;
import org.example.minishop.repository.OrderItemRepository;
import org.example.minishop.repository.OrderRepository;
import org.example.minishop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(UserService userService, OrderRepository orderRepository,  ProductRepository productRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    private OrderItemResponse orderItemToResponse(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getPrice());
        return orderItemResponse;
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getOrderItems().stream().map(this::orderItemToResponse).toList());
        return orderResponse;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){
        Order order = new Order();
        order.setUser(userService.getCurrentUser());
        order.setStatus(OrderStatus.CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(BigDecimal.ZERO);

        request.items().forEach(item -> {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));

            if(item.quantity() > product.getStock()) {
                throw new InsufficientItemsInStock(product.getStock());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.quantity());
            orderItem.setPrice(product.getPrice());
            order.getOrderItems().add(orderItem);

            product.setStock(product.getStock() - item.quantity());

            BigDecimal productTotalPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
            order.setTotalPrice(order.getTotalPrice().add(productTotalPrice));
        });

        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);

    }

    @Transactional
    public List<OrderResponse> getAllOrders(){
        User user = userService.getCurrentUser();
        List<Order> orders = orderRepository.findByUserId(user.getId());
        return orders.stream().map(this::toResponse).toList();
    }

    @Transactional
    public OrderResponse getOrderById(Long id){
        User user = userService.getCurrentUser();
        Order order = orderRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new OrderNotFoundException(id));
        return toResponse(order);
    }

    @Transactional
    public void cancelOrder(Long id){
        User user = userService.getCurrentUser();
        Order order = orderRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderAlreadyCancelledException(id);
        }

        order.getOrderItems().forEach(orderItem -> {
            Integer productQuantity = orderItem.getQuantity();
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + productQuantity);
        });
        order.setStatus(OrderStatus.CANCELLED);
    }
}
