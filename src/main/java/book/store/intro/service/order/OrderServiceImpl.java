package book.store.intro.service.order;

import book.store.intro.dto.order.request.CreateOrderRequestDto;
import book.store.intro.dto.order.request.UpdateOrderItemDto;
import book.store.intro.dto.order.response.OrderResponseDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.order.OrderItemMapper;
import book.store.intro.mapper.order.OrderMapper;
import book.store.intro.model.Book;
import book.store.intro.model.CartItem;
import book.store.intro.model.Order;
import book.store.intro.model.OrderItem;
import book.store.intro.model.ShoppingCart;
import book.store.intro.repository.order.OrderRepository;
import book.store.intro.service.shoppingcart.CartItemService;
import book.store.intro.service.shoppingcart.ShoppingCartService;
import book.store.intro.service.user.UserService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final CartItemService cartItemService;
    private final UserService userService;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser();
        Order order = orderMapper.toOrder(shoppingCart);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setTotal(getTotal(shoppingCart));
        Order savedOrder = orderRepository.save(order);

        Set<OrderItem> orderItems = getOrderItemsFromShoppingCart(shoppingCart);
        shoppingCart.getCartItems().forEach(item -> cartItemService.deleteById(item.getId()));
        orderItems.forEach(item -> item.setOrder(savedOrder));
        orderItems.forEach(orderItemService::save);
        savedOrder.setOrderItems(orderItems);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAllByUser() {
        return orderRepository.findAllByUser(userService.getAuthenticatedUser())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(Long id, UpdateOrderItemDto itemDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find order by id " + id));
        order.setStatus(itemDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(CartItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<OrderItem> getOrderItemsFromShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toOrderItem)
                .collect(Collectors.toSet());
    }

}
