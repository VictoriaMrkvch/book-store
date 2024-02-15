package book.store.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.intro.dto.book.category.CategoryDto;
import book.store.intro.dto.book.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CategoryControllerTest {
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
    @DisplayName("Create category - expected result: create category in DB, "
            + "return created category")
    @Sql(scripts = {
            "classpath:database/controller/category/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("name")
                .setDescription("description");
        CategoryDto expected = new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual,"id");
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "classpath:database/controller/category/insert-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/controller/category/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get list of existing categories - expected result: list of categories")
    public void getAllCategories_ReturnListOfExistingCategories() throws Exception {
        //When
        MvcResult result = mockMvc.perform(
                        get("/api/categories"))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = List.of(objectMapper.readValue(result
                .getResponse().getContentAsByteArray(), CategoryDto[].class));
        //Then
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
    @Sql(scripts = {
            "classpath:database/controller/category/insert-two-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/controller/category/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find category by existing in DB id - expected result: return category")
    public void findById_ValidId_ReturnCategory() throws Exception {
        //Given
        Long expectedId = 1L;

        CategoryDto expected = new CategoryDto()
                .setId(expectedId)
                .setName("Horror")
                .setDescription("Scary fantasy story");
        //When
        MvcResult result = mockMvc.perform(
                        get("/api/categories/{categoryId}", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto.class);
        //Then
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
