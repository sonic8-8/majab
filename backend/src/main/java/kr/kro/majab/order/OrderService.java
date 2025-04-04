package kr.kro.majab.order;

import kr.kro.majab.order.request.CreateOrderRequest;
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

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {

        Order order = Order.createOrder(LocalDateTime.now(), request.getItems(), request.getQuantity());

        order.changeStore(storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new NoSuchElementException("해당 가게가 존재하지 않습니다")));
        order.changeUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다")));

        Order savedOrder = orderRepository.save(order);

        return CreateOrderResponse.of(savedOrder);
    }
}
