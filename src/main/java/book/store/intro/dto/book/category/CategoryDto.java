package book.store.intro.dto.book.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
