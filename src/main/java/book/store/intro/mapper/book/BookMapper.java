package book.store.intro.mapper.book;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.BookDtoWithoutCategoryIds;
import book.store.intro.dto.book.CreateBookRequestDto;
import book.store.intro.model.Book;
import book.store.intro.model.Category;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        return Optional.ofNullable(id)
               .map(Book::new)
               .orElse(null);
    }
}
