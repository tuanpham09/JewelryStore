package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllActiveCategories();
    CategoryDto getCategoryById(Long id);
}
