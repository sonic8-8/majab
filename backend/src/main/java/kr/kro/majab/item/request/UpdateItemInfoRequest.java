package kr.kro.majab.item.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateItemInfoRequest {

    private Long itemId;

    @NotEmpty(message = "할인 전 가격 입력은 필수입니다")
    private int originalPrice;

    @NotEmpty(message = "할인 후 가격 입력은 필수입니다")
    private int discountedPrice;

    @NotEmpty(message = "상품 설명은 필수입니다")
    private String description;
}
