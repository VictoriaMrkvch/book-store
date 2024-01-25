package book.store.intro.service.order;

import book.store.intro.dto.order.response.OrderItemResponseDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.order.OrderItemMapper;
import book.store.intro.model.OrderItem;
import book.store.intro.repository.order.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemResponseDto save(OrderItem orderItem) {
        return orderItemMapper.toDto(orderItemRepository.save(orderItem));
    }

    @Override
    public List<OrderItemResponseDto> getAllFromOrder(Long id) {
        return orderItemRepository.getAllByOrderId(id)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getFromOrderById(Long orderId, Long id) {
        return orderItemMapper.toDto(orderItemRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item")));
    }
}
