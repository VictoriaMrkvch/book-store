package book.store.intro.service.shoppingcart;

import book.store.intro.dto.shoppingcart.response.ShoppingCartResponseDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.shoppingcart.ShoppingCartMapper;
import book.store.intro.model.ShoppingCart;
import book.store.intro.model.User;
import book.store.intro.repository.shoppingcart.ShoppingCartRepository;
import book.store.intro.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        User user = userService.getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart by id "
                        + user.getId()));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCart getShoppingCartByUser() {
        User user = userService.getAuthenticatedUser();
        return shoppingCartRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart by id "
                        + user.getId()));
    }
}
