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
