package kr.kro.majab.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import kr.kro.majab.item.Item;
import kr.kro.majab.store.StoreStatus;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class CreateOrderRequest {

    @NotEmpty(message = "주문자의 id는 필수값입니다")
    private Long userId;

    @NotEmpty(message = "가게의 id는 필수값입니다")
    private Long storeId;

    @NotEmpty(message = "가게 상태는 필수값입니다")
    private StoreStatus storeStatus;

    @NotEmpty(message = "상품은 필수값입니다")
    private ArrayList<Item> items;

    @NotEmpty(message = "상품 수량은 필수값입니다")
    @Min(value = 1, message = "상품 수량은 1개 이상 주문해주세요")
    private int quantity;
}
