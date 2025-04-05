package kr.kro.majab.order;

import kr.kro.majab.item.Item;
import kr.kro.majab.item.ItemRepository;
import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.order.response.CreateOrderResponse;
import kr.kro.majab.store.Store;
import kr.kro.majab.store.StoreRepository;
import kr.kro.majab.store.StoreStatus;
import kr.kro.majab.user.User;
import kr.kro.majab.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private static Store createStore(String name, StoreStatus status) {
        return Store.builder()
                .name(name)
                .storeStatus(status)
                .build();
    }

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

        Store store = createStore("가게", StoreStatus.OPEN);
        storeRepository.save(store);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .itemId(item.getId())
                .storeId(store.getId())
                .userId(user.getId())
                .quantity(5)
                .storeStatus(store.getStoreStatus())
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
    void createOrderStockException() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Item item = createItem(10, 3000);
        itemRepository.save(item);

        Store store = createStore("가게", StoreStatus.OPEN);
        storeRepository.save(store);

        User user = createUser("사용자");
        userRepository.save(user);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .userId(user.getId())
                .storeId(store.getId())
                .itemId(item.getId())
                .quantity(11)
                .storeStatus(store.getStoreStatus())
                .build();

        // when then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 재고가 부족합니다");
    }

    @DisplayName("가게가 운영 상태가 아닐 경우 주문하면 예외가 발생한다")
    @Test
    void createOrderStoreStatusException() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Item item = createItem(10, 3000);
        itemRepository.save(item);

        Store store = createStore("가게", StoreStatus.CLOSE);
        storeRepository.save(store);

        User user = createUser("사용자");
        userRepository.save(user);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .quantity(5)
                .itemId(item.getId())
                .storeId(store.getId())
                .userId(user.getId())
                .storeStatus(store.getStoreStatus())
                .build();

        // when then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("가게가 운영 중이지 않습니다");
    }

    @DisplayName("재고 수량이 0인 상품을 주문할 경우 예외가 발생한다")
    @Test
    void createOrderExceptionZeroQuantity() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Item item = createItem(0, 3000);
        itemRepository.save(item);

        Store store = createStore("가게", StoreStatus.OPEN);
        storeRepository.save(store);

        User user = createUser("사용자");
        userRepository.save(user);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .quantity(5)
                .itemId(item.getId())
                .storeId(store.getId())
                .userId(user.getId())
                .storeStatus(store.getStoreStatus())
                .build();

        // when then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품 재고가 없습니다");
    }

}