package book.store.intro.service.shoppingcart;

import book.store.intro.dto.shoppingcart.request.CreateCartItemRequestDto;
import book.store.intro.dto.shoppingcart.request.UpdateCartItemRequestDto;
import book.store.intro.dto.shoppingcart.response.CartItemResponseDto;

public interface CartItemService {
    CartItemResponseDto addCartItem(CreateCartItemRequestDto requestDto);

    CartItemResponseDto updateQuantity(Long id, UpdateCartItemRequestDto requestDto);

    void deleteById(Long id);
}
