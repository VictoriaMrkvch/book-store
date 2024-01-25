package book.store.intro.dto.shoppingcart.request;

import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    private Long bookId;
    private int quantity;
}
