package kr.kro.majab.order.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCancelOrderRequest {

    /**
     * todo: MVP 이후 JWT에서 userId 가져와 검증하도록 변경 필요
     */
    @NotNull(message = "사용자 id는 필수값입니다")
    private Long userId;

    @NotNull(message = "주문 id는 필수값입니다")
    private Long orderId;

    @Builder
    public UserCancelOrderRequest(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }
}
