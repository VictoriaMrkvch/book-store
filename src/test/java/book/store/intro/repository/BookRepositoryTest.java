package book.store.intro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import book.store.intro.model.Book;
import book.store.intro.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books with category")
    @Sql(scripts = {
            "classpath:database/repository/insert_twoBooksWithDifferentCategories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/repository/remove_booksWithCategories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ReturnsListWithOneBook() {
        List<Book> actual = bookRepository.findAllByCategoryId(1L);
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Find all books with category")
    @Sql(scripts = {
            "classpath:database/repository/insert_twoBooksWithSameCategory.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/repository/remove_booksWithSameCategory.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoryId_ValidCategoryId_ReturnsListWithTwoBooks() {
        List<Book> actual = bookRepository.findAllByCategoryId(1L);
        assertEquals(2, actual.size());
    }
}
