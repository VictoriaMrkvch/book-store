package book.store.intro.mapper.book;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.book.category.CategoryDto;
import book.store.intro.dto.book.category.CreateCategoryRequestDto;
import book.store.intro.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);
}
