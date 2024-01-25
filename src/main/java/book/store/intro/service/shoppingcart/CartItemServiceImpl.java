package book.store.intro.service.shoppingcart;

import book.store.intro.dto.shoppingcart.request.CreateCartItemRequestDto;
import book.store.intro.dto.shoppingcart.request.UpdateCartItemRequestDto;
import book.store.intro.dto.shoppingcart.response.CartItemResponseDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.shoppingcart.CartItemMapper;
import book.store.intro.model.Book;
import book.store.intro.model.CartItem;
import book.store.intro.model.ShoppingCart;
import book.store.intro.model.User;
import book.store.intro.repository.book.BookRepository;
import book.store.intro.repository.shoppingcart.CartItemRepository;
import book.store.intro.repository.shoppingcart.ShoppingCartRepository;
import book.store.intro.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final BookRepository bookRepository;

    @Override
    public CartItemResponseDto addCartItem(CreateCartItemRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id "
                        + requestDto.getBookId()));
        CartItem cartItem = cartItemMapper.toModel(requestDto, book, getShoppingCart());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto updateQuantity(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find cart item by id " + id));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private ShoppingCart getShoppingCart() {
        User user = userService.getAuthenticatedUser();
        return shoppingCartRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id "
                        + user.getId()));
    }
}
