package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.entity.Category;
import org.example.trangsuc_js.repository.CategoryRepository;
import org.example.trangsuc_js.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllActiveCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> category.getIsActive() != null && category.getIsActive())
                .sorted((c1, c2) -> {
                    Integer sort1 = c1.getSortOrder() != null ? c1.getSortOrder() : 0;
                    Integer sort2 = c2.getSortOrder() != null ? c2.getSortOrder() : 0;
                    return sort1.compareTo(sort2);
                })
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toCategoryDto(category);
    }
    
    private CategoryDto toCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setIsActive(category.getIsActive());
        dto.setSortOrder(category.getSortOrder());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        // Đặt productCount = 0 để tránh lỗi lazy loading
        dto.setProductCount(0L);
        
        return dto;
    }
}
