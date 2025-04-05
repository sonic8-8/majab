package kr.kro.majab.order.response;

import kr.kro.majab.order.Order;
import kr.kro.majab.order_item.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateOrderResponse {

    private Long id;

    private int totalPrice;

    private int quantity;

    private LocalDateTime registeredDateTime;

    @Builder
    public CreateOrderResponse(Long id, int totalPrice, int quantity, LocalDateTime registeredDateTime) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.registeredDateTime = registeredDateTime;
    }

    public static CreateOrderResponse of(Order order) {
        return CreateOrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.calculateTotalPrice(order.getOrderItems()))
                .quantity(order.getOrderItems().stream()
                        .mapToInt(OrderItem::getQuantity)
                        .sum())
                .registeredDateTime(order.getRegisteredDateTime())
                .build();
    }
}
