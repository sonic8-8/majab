package kr.kro.majab.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {

    @DisplayName("제공된 정보로 상품 정보를 업데이트 할 수 있다")
    @Test
    void updateItemInfo() {
        // given
        Item item = Item.builder().build();

        // when
        item.updateInfo(10000, 5000, "빵으로 구성돼있습니다");

        // then
        assertThat(item.getOriginalPrice()).isEqualTo(10000);
        assertThat(item.getDiscountedPrice()).isEqualTo(5000);
        assertThat(item.getDescription()).isEqualTo("빵으로 구성돼있습니다");
    }

    @DisplayName("제공된 수량만큼 상품 재고를 업데이트 할 수 있다")
    @Test
    void updateItemStock() {
        // given
        Item item = Item.builder().build();

        // when
        item.updateStock(100);

        // then
        assertThat(item.getStock()).isEqualTo(100);
    }

    @DisplayName("재고가 제공된 수량보다 적은지 체크한다")
    @Test
    void isQuantityLessThan() {
        // given
        Item item = Item.builder()
                .stock(10)
                .build();

        // when
        boolean result = item.isStockLessThan(3);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("재고를 제공된 수량만큼 차감할 수 있다")
    @Test
    void deductQuantity() {
        // given
        Item item = Item.builder()
                .stock(10)
                .build();

        // when
        item.deductStock(3);

        // then
        assertThat(item.getStock()).isEqualTo(7);
    }

    @DisplayName("재고보다 많은 수량을 차감할 경우 예외가 발생한다")
    @Test
    void deductQuantity_exception() {
        // given
        Item item = Item.builder()
                .stock(10)
                .build();

        // when then
        assertThatThrownBy(() -> item.deductStock(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고 수가 부족합니다");
    }

}