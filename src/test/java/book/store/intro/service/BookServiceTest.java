package book.store.intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.CreateBookRequestDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.book.BookMapper;
import book.store.intro.model.Book;
import book.store.intro.repository.book.BookRepository;
import book.store.intro.repository.book.CategoryRepository;
import book.store.intro.service.book.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Pank 57");
        requestDto.setAuthor("Penelopa Duglas");
        requestDto.setIsbn("unique isbn");
        requestDto.setPrice(BigDecimal.valueOf(220.55));
        requestDto.setDescription("Description");
        requestDto.setCoverImage("cover_image.jpg");
        requestDto.setCategoriesIds(List.of());

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategories(Set.of());

        BookDto bookDto = getBookDtoFromBook(book);
        bookDto.setId(1L);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(any())).thenReturn(List.of());
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto savedBookDto = bookService.save(requestDto);

        //Then
        assertThat(savedBookDto).isEqualTo(bookDto);

        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_ValidPageable_ReturnsAllBookDto() {
        //Given
        Book book = getDefaultBook();
        book.setId(1L);

        BookDto bookDto = getBookDtoFromBook(book);
        bookDto.setId(1L);

        Pageable pageable = PageRequest.of(0,10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        List<BookDto> bookDtos = bookService.findAll(pageable);

        //Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findById() method works")
    public void findById_ValidBookId_ReturnsBookDto() {
        //Given
        Long bookId = 1L;

        Book book = getDefaultBook();
        book.setId(bookId);

        BookDto bookDto = getBookDtoFromBook(book);
        bookDto.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // When
        BookDto bookById = bookService.findById(bookId);

        //Then
        assertThat(bookById).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findById() method works with non existing book id")
    public void findById_WithNonExistingBookId_ShouldThrowException() {
        // Given
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(bookId));

        // Then
        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify updateBookById() method works with non existing book id")
    public void updateBookById_WithNonExistingBookId_ShouldThrowException() {
        // Given
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBookById(bookId, new CreateBookRequestDto()));

        // Then
        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    private Book getDefaultBook() {
        return new Book().setTitle("Pank 57")
                .setAuthor("Penelopa Duglas")
                .setIsbn("unique isbn")
                .setPrice(BigDecimal.valueOf(220.55))
                .setDescription("Description")
                .setCoverImage("cover_image.jpg")
                .setCategories(Set.of());
    }

    private BookDto getBookDtoFromBook(Book book) {
        return new BookDto().setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage())
                .setCategoriesIds(List.of());
    }
}
