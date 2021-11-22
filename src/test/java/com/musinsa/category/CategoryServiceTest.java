package com.musinsa.category;

import com.musinsa.category.controller.dto.CategoryDto;
import com.musinsa.category.domain.entity.Category;
import com.musinsa.category.domain.repository.CategoryRepository;
import com.musinsa.category.service.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    //SavedID
    private CategoryDto createCategoryDTO(String testBranch, String testCode, String testName) {
        CategoryDto categoryDTO = new CategoryDto();
        categoryDTO.setBranch(testBranch);
        categoryDTO.setCode(testCode);
        categoryDTO.setName(testName);
        return categoryDTO;
    }

    private Category findCategory(Long savedId) {
        return categoryRepository.findById(savedId).orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void saveCategory() {
        //given
        CategoryDto categoryDTO = createCategoryDTO("TestBranch", "TestCode", "TestName");
        Long savedId = categoryService.saveCategory(categoryDTO);

        //when
        Category category = findCategory(savedId);

        //then
        assert(category.getCode().equals("TestCode"));
    }

    @Test
    public void updateCategory() {
        //given
        CategoryDto categoryDTO = createCategoryDTO("TestBranch", "TestCode", "TestName");
        Long savedId = categoryService.saveCategory(categoryDTO);

        Category category = findCategory(savedId);
        CategoryDto targetCategory = new CategoryDto(category);
        targetCategory.setName("UpdateCategory");

        //when

        Long updateId = categoryService.updateCategory("TestBranch", "TestCode", targetCategory);
        Category updatedCategory = findCategory(updateId);

        //then
        assert(updatedCategory.getName().equals("UpdateCategory"));
    }


}