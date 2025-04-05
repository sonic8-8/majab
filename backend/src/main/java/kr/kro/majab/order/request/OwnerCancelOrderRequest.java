package kr.kro.majab.order.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OwnerCancelOrderRequest {

    /**
     * todo: MVP 이후 JWT에서 ownerId 가져와 검증하도록 변경 필요
     */
    @NotNull(message = "사장님 id는 필수값입니다")
    private Long ownerId;

    @NotNull(message = "주문 id는 필수값입니다")
    private Long orderId;
}
