package book.store.intro.service.order;

import book.store.intro.dto.order.request.CreateOrderRequestDto;
import book.store.intro.dto.order.request.UpdateOrderItemDto;
import book.store.intro.dto.order.response.OrderResponseDto;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto requestDto);

    List<OrderResponseDto> getAllByUser();

    OrderResponseDto updateStatus(Long id, UpdateOrderItemDto itemDto);
}
