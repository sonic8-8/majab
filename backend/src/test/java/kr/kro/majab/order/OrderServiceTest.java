package kr.kro.majab.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.kro.majab.item.Item;
import kr.kro.majab.item.ItemRepository;
import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.order.request.OwnerCancelOrderRequest;
import kr.kro.majab.order.request.UserCancelOrderRequest;
import kr.kro.majab.order.response.CreateOrderResponse;
import kr.kro.majab.order.response.OwnerCancelOrderResponse;
import kr.kro.majab.order.response.UserCancelOrderResponse;
import kr.kro.majab.owner.Owner;
import kr.kro.majab.store.Store;
import kr.kro.majab.store.StoreRepository;
import kr.kro.majab.store.StoreStatus;
import kr.kro.majab.user.User;
import kr.kro.majab.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
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

    private Owner createOwner(String email) {
        return Owner.builder()
                .email(email)
                .build();
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        itemRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("주문 생성")
    class createOrder {

        @DisplayName("사용자는 주문을 생성할 수 있다")
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
            assertThat(orderResponse.getOrderId()).isNotNull();
            assertThat(orderResponse.getQuantity()).isEqualTo(5);
            assertThat(orderResponse.getTotalPrice()).isEqualTo(15000);
            assertThat(orderResponse.getRegisteredDateTime()).isEqualTo(registeredDateTime);
        }

        @DisplayName("재고 수량이 부족한 제품을 주문할 경우 예외가 발생한다")
        @Test
        void createOrder_StockException() {
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

        @DisplayName("존재하지 않는 사용자가 주문할 경우 예외가 발생한다")
        @Test
        void createOrderException_UserNotExist() {
            // given
            LocalDateTime registeredDateTime = LocalDateTime.now();

            Item item = createItem(10, 3000);
            itemRepository.save(item);

            Store store = createStore("가게", StoreStatus.OPEN);
            storeRepository.save(store);

            CreateOrderRequest request = CreateOrderRequest.builder()
                    .storeStatus(store.getStoreStatus())
                    .itemId(item.getId())
                    .storeId(store.getId())
                    .quantity(5)
                    .userId(Long.MAX_VALUE)
                    .build();

            // when then
            assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 사용자가 존재하지 않습니다");
        }

        @DisplayName("존재하지 않는 가게의 상품을 주문할 경우 예외가 발생한다")
        @Test
        void createOrderException_StoreNotExist() {
            // given
            LocalDateTime registeredDateTime = LocalDateTime.now();

            Item item = createItem(10, 3000);
            itemRepository.save(item);

            User user = createUser("사용자");
            userRepository.save(user);

            CreateOrderRequest request = CreateOrderRequest.builder()
                    .storeStatus(StoreStatus.OPEN)
                    .itemId(item.getId())
                    .storeId(Long.MAX_VALUE)
                    .quantity(5)
                    .userId(user.getId())
                    .build();

            // when then
            assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 가게가 존재하지 않습니다");
        }

        @DisplayName("존재하지 않는 상품을 주문할 경우 예외가 발생한다")
        @Test
        void createOrderException_ItemNotExist() {
            // given
            LocalDateTime registeredDateTime = LocalDateTime.now();

            Store store = createStore("가게", StoreStatus.OPEN);
            storeRepository.save(store);

            User user = createUser("사용자");
            userRepository.save(user);

            CreateOrderRequest request = CreateOrderRequest.builder()
                    .storeStatus(store.getStoreStatus())
                    .itemId(Long.MAX_VALUE)
                    .storeId(store.getId())
                    .quantity(5)
                    .userId(user.getId())
                    .build();

            // when then
            assertThatThrownBy(() -> orderService.createOrder(request, registeredDateTime))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("해당 상품이 존재하지 않습니다");
        }

        @DisplayName("재고 수량 부족으로 예외 발생시 주문이 저장되지 않는다")
        @Test
        void createOrder_rollback() {
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

            assertThat(orderRepository.findAll()).isEmpty();
        }

        /**
         * 동시성 테스트: 필요 시 수동으로 실행하기
         *
         * @Transactional 주석 처리 후
         * @TestInstance(TestInstance.Life_cycle.PER_CLASS) 설정 후 실행
         */
//    Item item;
//    Store store;
//    User user;
//
//    @BeforeAll
//    void setUp() {
//        item = createItem(10, 3000);
//        itemRepository.saveAndFlush(item);
//
//        store = createStore("가게", StoreStatus.OPEN);
//        storeRepository.saveAndFlush(store);
//
//        user = createUser("사용자");
//        userRepository.saveAndFlush(user);
//    }
//
//    @DisplayName("동시 주문 요청 시 재고 수량이 정확히 감소한다")
//    @Test
//    void concurrent_createOrder_with_pessimistic_lock() throws InterruptedException {
//        // given
//        int threadCount = 10;
//        ExecutorService executor = Executors.newFixedThreadPool(10);
//        CountDownLatch latch = new CountDownLatch(10);
//
//        LocalDateTime registeredDateTime = LocalDateTime.now();
//
//        // when
//        for (int i = 0; i < threadCount; i++) {
//            executor.submit(() -> {
//                try {
//                    log.info("request 생성 전");
//                    CreateOrderRequest request = CreateOrderRequest.builder()
//                            .itemId(item.getId())
//                            .storeId(store.getId())
//                            .userId(user.getId())
//                            .quantity(1)
//                            .storeStatus(store.getStoreStatus())
//                            .build();
//
//                    log.info("request 생성 후");
//                    log.info("저장된 가게: {}", storeRepository.findById(store.getId()).toString());
//
//                    orderService.createOrder(request, registeredDateTime);
//                } catch (Exception e) {
//                    log.error("예외 발생: {}", e.getMessage());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await(10, TimeUnit.SECONDS);
//
//        // then
//        Item updatedItem = itemRepository.findAll().get(0);
//        assertThat(updatedItem.getStock()).isEqualTo(0);
//    }
    }

    @Nested
    @DisplayName("주문 취소")
    class CancelOrder {

        @Nested
        @DisplayName("사용자 주문 취소")
        class UserCancel {

            User user;
            Store store;
            Item item;
            Order order;

            @PersistenceContext
            EntityManager em;

            @BeforeEach
            void setUp() {
                LocalDateTime registeredDateTime = LocalDateTime.now();

                user = createUser("사용자");
                userRepository.save(user);

                store = createStore("가게", StoreStatus.OPEN);
                storeRepository.save(store);

                item = createItem(10, 3000);
                itemRepository.save(item);

                CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                        .itemId(item.getId())
                        .storeStatus(store.getStoreStatus())
                        .storeId(store.getId())
                        .quantity(5)
                        .userId(user.getId())
                        .build();

                CreateOrderResponse orderResponse = orderService.createOrder(orderRequest, registeredDateTime);

                order = orderRepository.findById(orderResponse.getOrderId())
                        .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
            }

            @DisplayName("사용자는 주문을 취소할 수 있다")
            @Test
            void cancelOrderByUser() {
                // given
                User findUser = userRepository.findById(user.getId()).orElseThrow();
                Order findOrder = orderRepository.findById(order.getId()).orElseThrow();

                UserCancelOrderRequest cancelRequest = UserCancelOrderRequest.builder()
                        .orderId(findOrder.getId())
                        .userId(findUser.getId())
                        .build();

                // when
                UserCancelOrderResponse cancelResponse = orderService.cancelOrderByUser(cancelRequest);

                // then
                assertThat(cancelResponse.getOrderId()).isEqualTo(findOrder.getId());
                assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);

                Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
                assertThat(updatedItem.getStock()).isEqualTo(10);
            }

            @DisplayName("존재하지 않는 사용자가 주문 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByUser_notExistUser() {
                // given
                Order findOrder = orderRepository.findById(order.getId()).orElseThrow();

                UserCancelOrderRequest cancelRequest = UserCancelOrderRequest.builder()
                        .orderId(findOrder.getId())
                        .userId(Long.MAX_VALUE)
                        .build();

                // when then
                assertThatThrownBy(() -> orderService.cancelOrderByUser(cancelRequest))
                        .isInstanceOf(NoSuchElementException.class)
                        .hasMessage("해당 사용자가 존재하지 않습니다");
            }

            @DisplayName("존재하지 않는 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByUser_notExistOrder() {
                // given
                User findUser = userRepository.findById(user.getId()).orElseThrow();

                UserCancelOrderRequest cancelRequest = UserCancelOrderRequest.builder()
                        .orderId(Long.MAX_VALUE)
                        .userId(findUser.getId())
                        .build();

                // when then
                assertThatThrownBy(() -> orderService.cancelOrderByUser(cancelRequest))
                        .isInstanceOf(NoSuchElementException.class)
                        .hasMessage("해당 주문이 존재하지 않습니다");
            }

            @DisplayName("주문자와 사용자가 일치하지 않을 경우 예외가 발생한다")
            @Test
            void cancelOrderByUser_notEqualUser() {
                // given
                User nonOrderUser = createUser("주문하지 않은 사람");
                userRepository.save(nonOrderUser);
                Order findOrder = orderRepository.findById(order.getId()).orElseThrow();

                UserCancelOrderRequest request = UserCancelOrderRequest.builder()
                        .orderId(findOrder.getId())
                        .userId(nonOrderUser.getId())
                        .build();

                // when then
                assertThatThrownBy(() -> orderService.cancelOrderByUser(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("사용자와 주문자가 일치하지 않습니다");
            }

            @DisplayName("주문 상태가 취소인 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByUser_orderStatusCancel() {
                // given
                order.updateOrderStatus(OrderStatus.CANCELED);

                em.flush();
                em.clear();

                User findUser = userRepository.findById(user.getId()).orElseThrow();
                Order findOrder = orderRepository.findById(order.getId()).orElseThrow();

                UserCancelOrderRequest request = UserCancelOrderRequest.builder()
                        .orderId(findOrder.getId())
                        .userId(findUser.getId())
                        .build();

                // when then
                assertThatThrownBy(() -> orderService.cancelOrderByUser(request))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 취소된 주문입니다");
            }

            @DisplayName("주문 상태가 판매 완료인 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByUser_orderStatusCompleted() {
                // given
                order.updateOrderStatus(OrderStatus.COMPLETED);

                em.flush();
                em.clear();

                User findUser = userRepository.findById(user.getId()).orElseThrow();
                Order findOrder = orderRepository.findById(order.getId()).orElseThrow();

                UserCancelOrderRequest request = UserCancelOrderRequest.builder()
                        .orderId(findOrder.getId())
                        .userId(findUser.getId())
                        .build();

                // when then
                assertThatThrownBy(() -> orderService.cancelOrderByUser(request))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 판매 완료된 주문입니다");
            }
        }

        @Nested
        @DisplayName("사장님 주문 취소")
        class OwnerCancel {

            @DisplayName("사장님은 주문을 취소할 수 있다")
            @Test
            void cancelOrderByOwner() {
                // given
                LocalDateTime registeredDateTime = LocalDateTime.now();

                User user = createUser("사용자");
                userRepository.save(user);

                Store store = createStore("가게", StoreStatus.OPEN);
                storeRepository.save(store);

                Item item = createItem(10, 3000);
                itemRepository.save(item);

                CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                        .itemId(item.getId())
                        .storeStatus(store.getStoreStatus())
                        .storeId(store.getId())
                        .quantity(5)
                        .userId(user.getId())
                        .build();

                CreateOrderResponse orderResponse = orderService.createOrder(orderRequest, registeredDateTime);

                Order order = orderRepository.findById(orderResponse.getOrderId())
                        .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));

                OwnerCancelOrderRequest cancelRequest = OwnerCancelOrderRequest.builder()
                        .orderId(order.getId())
                        .storeId(store.getId())
                        .build();

                // when
                OwnerCancelOrderResponse cancelResponse = orderService.cancelOrderByOwner(cancelRequest);

                // then
                assertThat(cancelResponse.getOrderId()).isEqualTo(order.getId());
                assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);

                Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
                assertThat(updatedItem.getStock()).isEqualTo(10);
            }

            @DisplayName("존재하지 않는 가게가 주문 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByOwner_notExistStore() {
                // given

                // when

                // then

            }

            @DisplayName("존재하지 않는 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByOwner_notExistOrder() {
                // given

                // when

                // then

            }

            @DisplayName("주문 취소 요청 가게와 실제 주문의 가게가 일치하지 않을 경우 예외가 발생한다")
            @Test
            void cancelOrderByOwner_notEqualStore() {
                // given

                // when

                // then

            }

            @DisplayName("주문 상태가 취소인 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByOwner_orderStatusCancel() {
                // given

                // when

                // then

            }

            @DisplayName("주문 상태가 판매 완료인 주문을 취소할 경우 예외가 발생한다")
            @Test
            void cancelOrderByOwner_orderStatusCompleted() {
                // given

                // when

                // then

            }
        }

    }

}