package kr.kro.majab.order;

import kr.kro.majab.item.Item;
import kr.kro.majab.item.ItemRepository;
import kr.kro.majab.order.request.CancelOrderRequest;
import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.order.response.CancelOrderResponse;
import kr.kro.majab.order.response.CreateOrderResponse;
import kr.kro.majab.store.StoreRepository;
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

    /**
     * todo: 상품 주문 동시성 문제 해결
     */
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request, LocalDateTime registeredDateTime) {

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다"));

        if (item.getStock() < 1) {
            throw new RuntimeException("상품 재고가 없습니다");
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
}
