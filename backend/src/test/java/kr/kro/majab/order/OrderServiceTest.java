package kr.kro.majab.order;

import kr.kro.majab.item.Item;
import kr.kro.majab.item.ItemRepository;
import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.order.response.CreateOrderResponse;
import kr.kro.majab.order_item.OrderItemRepository;
import kr.kro.majab.store.Store;
import kr.kro.majab.store.StoreRepository;
import kr.kro.majab.user.User;
import kr.kro.majab.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 요청시 주문을 생성한다")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Item item = createItem(10, 3000);
        itemRepository.save(item);

        User user = createUser("사용자");
        userRepository.save(user);

        Store store = createStore("가게");
        storeRepository.save(store);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .itemId(item.getId())
                .storeId(store.getId())
                .userId(user.getId())
                .quantity(5)
                .build();

        // when
        CreateOrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getQuantity()).isEqualTo(5);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(15000);
        assertThat(orderResponse.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    @DisplayName("재고 수량이 부족한 제품을 주문할 경우 예외가 발생한다")
    @Test
    void test() {
        // given

        // when

        // then

    }

    private static Item createItem(int stock, int price) {
        return Item.builder()
                .discountedPrice(price)
                .stock(stock)
                .build();
    }

    private static User createUser(String name) {
        return User.builder()
                .nickname(name)
                .build();
    }

    private static Store createStore(String name) {
        return Store.builder()
                .name(name)
                .build();
    }

}