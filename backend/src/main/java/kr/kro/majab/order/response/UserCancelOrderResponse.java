package kr.kro.majab.order.response;

import kr.kro.majab.order.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCancelOrderResponse {

    private Long orderId;

    @Builder
    public UserCancelOrderResponse(Long orderId) {
        this.orderId = orderId;
    }

    public static UserCancelOrderResponse of(Order order) {
        return UserCancelOrderResponse.builder()
                .orderId(order.getId())
                .build();
    }
}
