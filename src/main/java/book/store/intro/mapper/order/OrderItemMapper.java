package book.store.intro.mapper.order;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.order.response.OrderItemResponseDto;
import book.store.intro.model.CartItem;
import book.store.intro.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "item.book.id")
    OrderItemResponseDto toDto(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "price", source = "cartItem.book.price")
    OrderItem toOrderItem(CartItem cartItem);
}
