package book.store.intro.repository.order;

import book.store.intro.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> getAllByOrderId(Long id);

    Optional<OrderItem> findByOrderIdAndId(Long orderId, Long id);
}
