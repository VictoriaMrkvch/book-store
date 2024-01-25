package book.store.intro.dto.order.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank
    private String shippingAddress;
}
