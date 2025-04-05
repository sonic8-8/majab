package kr.kro.majab.item.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateItemStockRequest {

    @NotNull(message = "상품 id는 필수입니다")
    private Long itemId;

    /**
     * todo: MVP 이후 JWT에서 ownerId 가져와 검증하도록 변경 필요
     */
    @NotNull(message = "사장님 id는 필수입니다")
    private Long ownerId;

    @Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다")
    private int stock;

    public UpdateItemStockRequest(Long itemId, int stock) {
        this.itemId = itemId;
        this.stock = stock;
    }
}
