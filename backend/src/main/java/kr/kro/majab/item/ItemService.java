package kr.kro.majab.item;

import kr.kro.majab.item.request.UpdateItemInfoRequest;
import kr.kro.majab.item.request.UpdateItemStockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * todo: 사장님 회원이 업데이트 요청한건지 확인하는 로직 필요, JWT 같은 토큰으로 ownerId 받아와서 처리하기
     */
    @Transactional
    public void updateItemInfo(UpdateItemInfoRequest request) {

        Item findItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        findItem.updateInfo(request.getOriginalPrice(), request.getDiscountedPrice(), request.getDescription());

        itemRepository.save(findItem);
    }

    @Transactional
    public void updateItem(UpdateItemStockRequest request) {

        Item findItem = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

        findItem.updateStock(request.getStock());

        itemRepository.save(findItem);
    }

}
