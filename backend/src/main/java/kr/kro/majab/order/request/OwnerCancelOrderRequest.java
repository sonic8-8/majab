package kr.kro.majab.order.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OwnerCancelOrderRequest {

    /**
     * todo: ownerId는 컨트롤러에서 검증, ownerId와 store의 ownerId가 일치하는 확인하기
     */
    @NotNull(message = "가게 id는 필수값입니다")
    private Long storeId;

    @NotNull(message = "주문 id는 필수값입니다")
    private Long orderId;

    @Builder
    public OwnerCancelOrderRequest(Long storeId, Long orderId) {
        this.storeId = storeId;
        this.orderId = orderId;
    }
}
