package book.store.intro.service.shoppingcart;

import book.store.intro.dto.shoppingcart.response.ShoppingCartResponseDto;
import book.store.intro.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCart getShoppingCartByUser();
}
