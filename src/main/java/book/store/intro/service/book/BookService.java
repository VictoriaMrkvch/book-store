package book.store.intro.service.book;

import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
