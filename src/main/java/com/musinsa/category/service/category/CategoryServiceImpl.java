package com.musinsa.category.service.category;

import com.musinsa.category.controller.dto.CategoryDto;
import com.musinsa.category.domain.entity.Category;
import com.musinsa.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public Long saveCategory(CategoryDto categoryDTO) {

        Category category = categoryDTO.toEntity();
        //대분류 등록
        if (categoryDTO.getParentCategoryName() == null) {
            //JPA 사용하여 DB에서 branch와 name의 중복값을 검사. (대분류에서만 가능)
            if (categoryRepository.existsByBranchAndName(categoryDTO.getBranch(), categoryDTO.getName())) {
                throw new RuntimeException("branch와 name이 같을 수 없습니다. ");
            }

            Category rootCategory = categoryRepository.findByBranchAndName(categoryDTO.getBranch(), "ROOT")
                    .orElseGet(() ->
                            Category.builder()
                                    .name("ROOT")
                                    .code("ROOT")
                                    .branch(categoryDTO.getBranch())
                                    .level(0)
                                    .build()
                    );
            if (!categoryRepository.existsByBranchAndName(categoryDTO.getBranch(), "ROOT")) {
                categoryRepository.save(rootCategory);
            }
            category.setParentCategory(rootCategory);
            category.setLevel(1);
            //중, 소분류 등록
        } else {
            String parentCategoryName = categoryDTO.getParentCategoryName();
            Category parentCategory = categoryRepository.findByBranchAndName(categoryDTO.getBranch(), parentCategoryName)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리 없음"));
            category.setLevel(parentCategory.getLevel() + 1);
            category.setParentCategory(parentCategory);
            parentCategory.getSubCategory().add(category);
        }

        return categoryRepository.save(category).getId();
    }

    public Map<String, CategoryDto> getCategoryByBranch(String branch) {
        Category category = categoryRepository.findByBranchAndCode(branch, "ROOT")
                .orElseThrow(() -> new IllegalArgumentException("찾는 대분류가 없습니다"));

        CategoryDto categoryDTO = new CategoryDto(category);

        Map <String, CategoryDto> data = new HashMap<>();
        data.put(categoryDTO.getName(), categoryDTO);

        return data;
    }

    public void deleteCategory(String branch, String code) {
        Category category = findCategoryByBranchAndName(branch, code);

        if (category.getSubCategory().size() == 0) { //하위 카테고리 없을 경우
            Category parentCategory = findCategory(category.getParentCategory().getId());
            if (!parentCategory.getName().equals("ROOT")) { // ROOT가 아닌 다른 부모가 있을 경우
                parentCategory.getSubCategory().remove(category);
            }
            categoryRepository.deleteById(category.getId());
        } else { //하위 카테고리 있을 경우
            Category parentCategory = findCategory(category.getParentCategory().getId());
            //ROOT아닌 부모가 있을 경우
            if (!parentCategory.getName().equals("ROOT")) {
                parentCategory.getSubCategory().remove(category);
            }
            category.setName("Deleted category");
        }
    }

    public Long updateCategory(String branch, String code, CategoryDto categoryDto) {
        Category category = findCategoryByBranchAndName(branch, code);
        category.setName(categoryDto.getName());

        return category.getId();
    }

    private Category findCategoryByBranchAndName(String branch, String code) {
        return categoryRepository.findByBranchAndName(branch, code)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Category findCategory(Long savedId) {
        return categoryRepository.findById(savedId)
                .orElseThrow(IllegalArgumentException::new);
    }
}