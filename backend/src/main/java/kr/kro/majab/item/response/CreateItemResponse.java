package kr.kro.majab.item.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateItemResponse {

    private String name;

    private int quantity;

    @Builder
    public CreateItemResponse(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
