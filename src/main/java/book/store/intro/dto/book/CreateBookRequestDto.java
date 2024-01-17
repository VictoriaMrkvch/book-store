package book.store.intro.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Length(max = 255)
    private String title;
    @NotBlank
    @Length(max = 255)
    private String author;
    @NotBlank
    @Length(max = 255)
    private String isbn;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Length(max = 255)
    private String description;
    @NotNull
    @Length(max = 255)
    private String coverImage;
    @NotNull
    private List<Long> categoriesIds;
}
