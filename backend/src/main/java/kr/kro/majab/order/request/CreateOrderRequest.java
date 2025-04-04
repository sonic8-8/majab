package kr.kro.majab.order.request;

import jakarta.validation.constraints.NotEmpty;
import kr.kro.majab.store.StoreStatus;

public class CreateOrderRequest {

    @NotEmpty(message = "주문자의 id는 필수값입니다")
    private Long userId;

    @NotEmpty(message = "가게 상태는 필수값입니다")
    private StoreStatus storeStatus;

}
