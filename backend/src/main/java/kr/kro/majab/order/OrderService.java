package kr.kro.majab.order;

import kr.kro.majab.order.request.CreateOrderRequest;
import kr.kro.majab.store.StoreRepository;
import kr.kro.majab.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void createOrder(CreateOrderRequest request) {

        Order order = Order.createOrder(LocalDateTime.now(), request.getItems(), request.getQuantity());
        order.changeStore(request.getStoreId());
        order.changeUser(request.getUserId());


    }
}
