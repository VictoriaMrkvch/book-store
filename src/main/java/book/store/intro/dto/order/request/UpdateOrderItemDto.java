package book.store.intro.dto.order.request;

import book.store.intro.model.Status;
import lombok.Data;

@Data
public class UpdateOrderItemDto {
    private Status status;
}
