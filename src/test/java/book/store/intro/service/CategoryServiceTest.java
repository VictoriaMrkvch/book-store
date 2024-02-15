package book.store.intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import book.store.intro.dto.book.category.CategoryDto;
import book.store.intro.dto.book.category.CreateCategoryRequestDto;
import book.store.intro.exception.EntityNotFoundException;
import book.store.intro.mapper.book.CategoryMapper;
import book.store.intro.model.Category;
import book.store.intro.repository.book.CategoryRepository;
import book.store.intro.service.book.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateCategoryRequestDto_ReturnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Poetry");
        requestDto.setDescription("Description");

        Category category = new Category();
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        CategoryDto savedCategoryDto = categoryService.save(requestDto);

        // Then
        assertThat(savedCategoryDto).isEqualTo(categoryDto);

        verify(categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_ValidPageable_ReturnsAllCategoryDto() {
        //Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Poetry");
        category.setDescription("Description");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        Pageable pageable = PageRequest.of(0,10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);

        // Then
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method works")
    public void getById_ValidCategoryId_ReturnsCategoryDto() {
        // Given
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Poetry");
        category.setDescription("Description");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        CategoryDto categoryById = categoryService.getById(categoryId);

        // Then
        assertThat(categoryById).isEqualTo(categoryDto);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method works with non existing category id")
    public void getById_WithNonExistingCategoryId_ShouldThrowException() {
        // Given
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));

        // Then
        String expected = "Can't find category by id " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify update() method works with non existing category id")
    public void update_WithNonExistingCategoryId_ShouldThrowException() {
        // Given
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, new CreateCategoryRequestDto()));

        // Then
        String expected = "Can't find category by id " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
