package book.store.intro.dto.book.category;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
}
