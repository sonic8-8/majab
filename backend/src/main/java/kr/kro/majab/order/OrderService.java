package kr.kro.majab.order;

import kr.kro.majab.item.Item;
import kr.kro.majab.item.ItemRepository;
import kr.kro.majab.order.request.UserCancelOrderRequest;
import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.order.response.UserCancelOrderResponse;
import kr.kro.majab.order.response.CreateOrderResponse;
import kr.kro.majab.store.Store;
import kr.kro.majab.store.StoreRepository;
import kr.kro.majab.store.StoreStatus;
import kr.kro.majab.user.User;
import kr.kro.majab.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, LocalDateTime registeredDateTime) {

        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NoSuchElementException("해당 가게가 존재하지 않습니다"));

        if (store.getStoreStatus() != StoreStatus.OPEN) {
            throw new IllegalStateException("가게가 운영 중이지 않습니다");
        }

        Item item = itemRepository.findByIdWithPessimisticLock(request.getItemId())
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다"));

        if (item.getStock() < 1) {
            throw new IllegalStateException("상품 재고가 없습니다");
        }

        if (item.isStockLessThan(request.getQuantity())) {
            throw new IllegalArgumentException("상품 재고가 부족합니다");
        }
        item.deductStock(request.getQuantity());

        Order order = Order.createOrder(registeredDateTime, item, request.getQuantity());

        order.changeStore(storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NoSuchElementException("해당 가게가 존재하지 않습니다")));
        order.changeUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다")));

        Order savedOrder = orderRepository.save(order);

        return CreateOrderResponse.of(savedOrder);
    }

    @Transactional
    public UserCancelOrderResponse cancelOrderByUser(UserCancelOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다"));

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NoSuchElementException("해당 주문이 존재하지 않습니다"));

        if (order.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("해당 사용자와 주문자가 일치하지 않습니다");
        }

        order.updateOrderStatus(OrderStatus.CANCELED);
        order.getOrderItems()
                .forEach(orderItem -> orderItem.getItem().updateStock(orderItem.getItem().getStock() + orderItem.getQuantity()));

        //todo: 정산 기능 구현시 정산 내역 조정 로직 추가

        return UserCancelOrderResponse.of(order);
    }
}
