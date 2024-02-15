package book.store.intro.service.book;

import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.BookDtoWithoutCategoryIds;
import book.store.intro.dto.book.CreateBookRequestDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.book.BookMapper;
import book.store.intro.model.Book;
import book.store.intro.repository.book.BookRepository;
import book.store.intro.repository.book.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        List<Long> categoriesIds = requestDto.getCategoriesIds();
        categoryRepository.findAllById(categoriesIds).forEach(book::addCategory);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto requestDto) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id " + id));
        Book updatedBook = bookMapper.toModel(requestDto);
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
