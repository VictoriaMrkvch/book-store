package book.store.intro.service.order;

import book.store.intro.dto.order.response.OrderItemResponseDto;
import book.store.intro.model.OrderItem;
import java.util.List;

public interface OrderItemService {
    OrderItemResponseDto save(OrderItem orderItem);

    List<OrderItemResponseDto> getAllFromOrder(Long id);

    OrderItemResponseDto getFromOrderById(Long orderId, Long id);
}
