package kr.kro.majab.item.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateItemStockRequest {

    private Long itemId;

    @NotEmpty(message = "재고 수량 입력은 필수입니다")
    private int stock;

    public UpdateItemStockRequest(Long itemId, int stock) {
        this.itemId = itemId;
        this.stock = stock;
    }
}
