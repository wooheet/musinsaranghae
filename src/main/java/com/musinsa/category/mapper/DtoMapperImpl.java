package com.musinsa.category.mapper;

import com.musinsa.category.controller.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DtoMapperImpl implements DtoMapper {

    @Override
    public CategoryDto categoryDto(CategoryDto dto) {
        return CategoryDto.builder()
                .name(dto.getName())
                .build();
    }
}

