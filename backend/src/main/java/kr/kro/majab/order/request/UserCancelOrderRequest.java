package kr.kro.majab.order.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class UserCancelOrderRequest {

    @NotEmpty(message = "주문 id는 필수값입니다")
    private Long orderId;

    @NotEmpty(message = "사용자 id는 필수값입니다")
    private Long userId;

}
