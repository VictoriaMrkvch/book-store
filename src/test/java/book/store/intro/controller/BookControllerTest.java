package book.store.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.intro.dto.book.BookDto;
import book.store.intro.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create book - expected result: create book in DB, return created book")
    @Sql(scripts = {
            "classpath:database/controller/book/remove-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        // Given
        CreateBookRequestDto requestDto = getDefaultRequestDto();
        BookDto expected = getBookDto(requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual,"id");
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "classpath:database/controller/book/insert-two-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/controller/book/remove-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get list of existing books - expected result: list of books")
    public void getAllBooks_ReturnListOfExistingBooks() throws Exception {
        //When
        MvcResult result = mockMvc.perform(
                        get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsByteArray(), BookDto[].class));
        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "classpath:database/controller/book/insert-two-books.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/controller/book/remove-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find book by existing in DB id - expected result: return book")
    public void findById_ValidId_ReturnBook() throws Exception {
        //Given
        Long expectedId = 1L;
        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("1129831756563")
                .setPrice(BigDecimal.valueOf(49.90))
                .setDescription("horror novel")
                .setCoverImage("image.jpg");
        //When
        MvcResult result = mockMvc.perform(
                        get("/api/books/{bookId}", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), BookDto.class);
        //Then
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    private CreateBookRequestDto getDefaultRequestDto() {
        return new CreateBookRequestDto().setTitle("title")
                .setAuthor("Author")
                .setIsbn("978328755467364")
                .setPrice(BigDecimal.TEN)
                .setDescription("description")
                .setCoverImage("image.jpg")
                .setCategoriesIds(List.of());
    }

    private BookDto getBookDto(CreateBookRequestDto requestDto) {
        return new BookDto().setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoriesIds(List.of());
    }
}
