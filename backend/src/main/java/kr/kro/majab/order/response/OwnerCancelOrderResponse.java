package kr.kro.majab.order.response;

import kr.kro.majab.order.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerCancelOrderResponse {

    private Long orderId;

    @Builder
    public OwnerCancelOrderResponse(Long orderId) {
        this.orderId = orderId;
    }

    public static OwnerCancelOrderResponse of(Order order) {
        return OwnerCancelOrderResponse.builder()
                .orderId(order.getId())
                .build();
    }
}
