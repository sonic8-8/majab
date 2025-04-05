package kr.kro.majab.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import kr.kro.majab.item.Item;
import kr.kro.majab.store.StoreStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class CreateOrderRequest {

    /**
     * todo: MVP 이후 JWT에서 userId 가져와 검증하도록 변경 필요
     */
    @NotNull(message = "주문자의 id는 필수값입니다")
    private Long userId;

    @NotNull(message = "가게의 id는 필수값입니다")
    private Long storeId;

    @NotNull(message = "상품의 id는 필수값입니다")
    private Long itemId;

    @NotNull(message = "가게 상태는 필수값입니다")
    private StoreStatus storeStatus;

    @Min(value = 1, message = "상품 수량은 1개 이상 주문해주세요")
    private int quantity;

    @Builder
    public CreateOrderRequest(Long userId, Long storeId, Long itemId, StoreStatus storeStatus, int quantity) {
        this.userId = userId;
        this.storeId = storeId;
        this.itemId = itemId;
        this.storeStatus = storeStatus;
        this.quantity = quantity;
    }
}
