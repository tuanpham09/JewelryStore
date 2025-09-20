package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.search.ProductSearchDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.service.SearchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    
    private final ProductRepository productRepo;
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        // Simple implementation - just return all products
        List<Product> allProducts = productRepo.findAll();
        List<ProductDto> productDtos = allProducts.stream()
            .map(this::toProductDto)
            .collect(Collectors.toList());
        
        return new SearchResultDto<>(
            productDtos,
            (long) allProducts.size(),
            1,
            0,
            allProducts.size(),
            false,
            false,
            (long) allProducts.size(),
            "0ms"
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getFeaturedProducts(int page, int size) {
        List<Product> allProducts = productRepo.findAll();
        List<Product> featuredProducts = allProducts.stream()
            .filter(product -> product.getIsFeatured() != null && product.getIsFeatured())
            .collect(Collectors.toList());
        
        // Apply pagination
        int start = page * size;
        int end = Math.min(start + size, featuredProducts.size());
        List<Product> pagedProducts = featuredProducts.subList(start, end);
        
        List<ProductDto> productDtos = pagedProducts.stream()
            .map(this::toProductDto)
            .collect(Collectors.toList());
        
        return new SearchResultDto<>(
            productDtos,
            (long) featuredProducts.size(),
            (int) Math.ceil((double) featuredProducts.size() / size),
            page,
            size,
            end < featuredProducts.size(),
            page > 0,
            (long) featuredProducts.size(),
            "0ms"
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getNewProducts(int page, int size) {
        List<Product> allProducts = productRepo.findAll();
        List<Product> newProducts = allProducts.stream()
            .filter(product -> product.getIsNew() != null && product.getIsNew())
            .collect(Collectors.toList());
        
        int start = page * size;
        int end = Math.min(start + size, newProducts.size());
        List<Product> pagedProducts = newProducts.subList(start, end);
        
        List<ProductDto> productDtos = pagedProducts.stream()
            .map(this::toProductDto)
            .collect(Collectors.toList());
        
        return new SearchResultDto<>(
            productDtos,
            (long) newProducts.size(),
            (int) Math.ceil((double) newProducts.size() / size),
            page,
            size,
            end < newProducts.size(),
            page > 0,
            (long) newProducts.size(),
            "0ms"
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getBestsellerProducts(int page, int size) {
        List<Product> allProducts = productRepo.findAll();
        List<Product> bestsellerProducts = allProducts.stream()
            .filter(product -> product.getIsBestseller() != null && product.getIsBestseller())
            .collect(Collectors.toList());
        
        int start = page * size;
        int end = Math.min(start + size, bestsellerProducts.size());
        List<Product> pagedProducts = bestsellerProducts.subList(start, end);
        
        List<ProductDto> productDtos = pagedProducts.stream()
            .map(this::toProductDto)
            .collect(Collectors.toList());
        
        return new SearchResultDto<>(
            productDtos,
            (long) bestsellerProducts.size(),
            (int) Math.ceil((double) bestsellerProducts.size() / size),
            page,
            size,
            end < bestsellerProducts.size(),
            page > 0,
            (long) bestsellerProducts.size(),
            "0ms"
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getOnSaleProducts(int page, int size) {
        List<Product> allProducts = productRepo.findAll();
        List<Product> onSaleProducts = allProducts.stream()
            .filter(product -> product.isOnSale())
            .collect(Collectors.toList());
        
        int start = page * size;
        int end = Math.min(start + size, onSaleProducts.size());
        List<Product> pagedProducts = onSaleProducts.subList(start, end);
        
        List<ProductDto> productDtos = pagedProducts.stream()
            .map(this::toProductDto)
            .collect(Collectors.toList());
        
        return new SearchResultDto<>(
            productDtos,
            (long) onSaleProducts.size(),
            (int) Math.ceil((double) onSaleProducts.size() / size),
            page,
            size,
            end < onSaleProducts.size(),
            page > 0,
            (long) onSaleProducts.size(),
            "0ms"
        );
    }
    
    private ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setStock(product.getStock());
        dto.setThumbnail(product.getThumbnail());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsNew(product.getIsNew());
        dto.setIsBestseller(product.getIsBestseller());
        dto.setIsOnSale(product.isOnSale());
        dto.setBrand(product.getBrand());
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }
        
        return dto;
    }
}
