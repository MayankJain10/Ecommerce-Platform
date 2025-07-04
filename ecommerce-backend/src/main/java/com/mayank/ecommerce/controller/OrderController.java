package com.mayank.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayank.ecommerce.config.JwtUtil;
import com.mayank.ecommerce.entity.Order;
import com.mayank.ecommerce.entity.dto.OrderAnalyticsDTO;
import com.mayank.ecommerce.entity.dto.OrderRequestDTO;
import com.mayank.ecommerce.entity.dto.OrderResponseDTO;
import com.mayank.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/order")
@Tag(name = "Order Controller", description = "APIs for placing and viewing orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Place a new order manually", description = "Place an order with selected items and quantities.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order placed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or product not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/place/order")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @RequestBody OrderRequestDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String email = jwtUtil.extractUsername(authHeader.substring(7));
        Order order = orderService.placeOrder(dto, email);

        OrderResponseDTO response = new OrderResponseDTO(
            order.getId(),
            "Order placed successfully",
            order.getTotalAmount(),
            order.getOrderDate()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all orders of logged-in user", description = "Retrieve list of all previous orders placed by the user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orders fetched successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/getMy/order")
    public List<Order> getMyOrders(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        return orderService.getUserOrders(email);
    }

    @Operation(summary = "Place order from cart", description = "Converts all cart items to an order and clears the cart.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order placed successfully from cart"),
        @ApiResponse(responseCode = "400", description = "Cart is empty or user not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/place/fromCart")
    public ResponseEntity<OrderResponseDTO> placeOrderFromCart(@RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        Order order = orderService.placeOrderFromCart(email);

        OrderResponseDTO dto = new OrderResponseDTO(
                order.getId(),
                "Order placed successfully from cart",
                order.getTotalAmount(),
                order.getOrderDate()
        );

        return ResponseEntity.ok(dto);
    }
    
    @Operation(summary = "Delete orders", description = "Delete the current orders.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "400", description = "No order is present"),
    })
    
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String authHeader) {
        String email = jwtUtil.extractUsername(authHeader.substring(7));
        orderService.cancelOrder(orderId, email);
        return ResponseEntity.ok("Order cancelled successfully");
    }


}



