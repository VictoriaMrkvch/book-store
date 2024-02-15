package book.store.intro.dto.book.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private String description;
}
