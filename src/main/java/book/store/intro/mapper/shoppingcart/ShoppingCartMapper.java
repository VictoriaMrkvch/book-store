package book.store.intro.mapper.shoppingcart;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.shoppingcart.response.ShoppingCartResponseDto;
import book.store.intro.mapper.shoppingcart.impl.CartItemMapperImpl;
import book.store.intro.model.ShoppingCart;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    CartItemMapper cartItemMapper = new CartItemMapperImpl();
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setUserId(@MappingTarget ShoppingCartResponseDto responseDto,
                           ShoppingCart shoppingCart) {
        responseDto.setUserId(shoppingCart.getId());
        responseDto.setCartItems(shoppingCart.getCartItems()
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet()));
    }
}
