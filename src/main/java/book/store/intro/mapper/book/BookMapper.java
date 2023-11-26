package book.store.intro.mapper.book;

import book.store.intro.config.MapperConfig;
import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.CreateBookRequestDto;
import book.store.intro.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto book);
}
