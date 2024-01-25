package book.store.intro.mapper.order;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.order.response.OrderResponseDto;
import book.store.intro.mapper.order.impl.OrderItemMapperImpl;
import book.store.intro.model.Order;
import book.store.intro.model.ShoppingCart;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderItemMapper orderItemMapper = new OrderItemMapperImpl();

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "userId", source = "order.user.id")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "status", expression = "java(book.store.intro.model.Status.PENDING)")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    Order toOrder(ShoppingCart cart);

    @AfterMapping
   default void setOrderItemsToDto(@MappingTarget OrderResponseDto orderResponseDto,
                                   Order order) {
        orderResponseDto.setOrderItems(
                order.getOrderItems().stream()
                        .map(orderItemMapper::toDto)
                        .collect(Collectors.toSet()));
    }
}
