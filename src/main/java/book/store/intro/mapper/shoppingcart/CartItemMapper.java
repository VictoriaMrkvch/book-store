package book.store.intro.mapper.shoppingcart;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.shoppingcart.request.CreateCartItemRequestDto;
import book.store.intro.dto.shoppingcart.response.CartItemResponseDto;
import book.store.intro.mapper.book.BookMapper;
import book.store.intro.model.Book;
import book.store.intro.model.CartItem;
import book.store.intro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    CartItem toModel(CreateCartItemRequestDto requestDto,
                      Book book,
                      ShoppingCart shoppingCart);
}
