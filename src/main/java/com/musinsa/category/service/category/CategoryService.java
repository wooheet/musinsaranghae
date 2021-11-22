package com.musinsa.category.service.category;

import com.musinsa.category.controller.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CategoryService {
    Long saveCategory(CategoryDto categoryDTO);
    Map<String, CategoryDto> getCategoryByBranch(String branch);

    Long updateCategory(String branch, String code, CategoryDto categoryDto);
    void deleteCategory(String branch, String code);
}
