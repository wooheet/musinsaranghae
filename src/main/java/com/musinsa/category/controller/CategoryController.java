package com.musinsa.category.controller;

import com.musinsa.category.controller.dto.CategoryDto;
import com.musinsa.category.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping ("/categories")
    @ResponseBody
    public Long saveCategory (CategoryDto categoryDto) {
        return categoryService.saveCategory(categoryDto);
    }

    @GetMapping ("/categories/{branch}")
    @ResponseBody
    public Map<String, CategoryDto> getCategoryByBranch (@PathVariable String branch) {
        return categoryService.getCategoryByBranch(branch);
    }

    @PutMapping ("/categories/{branch}/{code}")
    @ResponseBody
    public Long updateCategory (@PathVariable (name = "branch") @NotBlank String branch,
                                @PathVariable (name = "code") @NotBlank String code,
                                CategoryDto categoryDto) {
        return categoryService.updateCategory(branch, code, categoryDto);
    }

    @DeleteMapping ("/categories/{branch}/{code}")
    @ResponseBody
    public void deleteCategory(@PathVariable (name = "branch") @NotBlank String branch,
                                @PathVariable (name = "code") @NotBlank String code) {
        categoryService.deleteCategory(branch, code);
    }

}
