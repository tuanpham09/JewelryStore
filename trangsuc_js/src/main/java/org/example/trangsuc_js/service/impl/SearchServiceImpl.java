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
        try {
            // Get all products
            List<Product> allProducts = productRepo.findAll();
            System.out.println("Total products in database: " + allProducts.size());
            System.out.println("Search DTO: " + searchDto);
            
            // Apply filters
            List<Product> filteredProducts = allProducts.stream()
                .filter(product -> {
                    // Only filter by isActive if explicitly set
                    if (searchDto.getIsActive() != null) {
                        return product.getIsActive() != null && product.getIsActive().equals(searchDto.getIsActive());
                    }
                    return true; // Don't filter if isActive is null
                })
                .filter(product -> {
                    String searchTerm = searchDto.getQuery() != null ? searchDto.getQuery() : searchDto.getKeyword();
                    if (searchTerm == null || searchTerm.trim().isEmpty()) {
                        return true; // No search term, include all
                    }
                    return product.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        (product.getDescription() != null && product.getDescription().toLowerCase().contains(searchTerm.toLowerCase()));
                })
                .filter(product -> searchDto.getCategoryId() == null || 
                    (product.getCategory() != null && product.getCategory().getId().equals(searchDto.getCategoryId())))
                .filter(product -> searchDto.getMinPrice() == null || 
                    product.getPrice().compareTo(searchDto.getMinPrice()) >= 0)
                .filter(product -> searchDto.getMaxPrice() == null || 
                    product.getPrice().compareTo(searchDto.getMaxPrice()) <= 0)
                .filter(product -> searchDto.getBrand() == null || searchDto.getBrand().trim().isEmpty() ||
                    (product.getBrand() != null && product.getBrand().equalsIgnoreCase(searchDto.getBrand())))
                .filter(product -> searchDto.getMaterial() == null || searchDto.getMaterial().trim().isEmpty() ||
                    (product.getMaterial() != null && product.getMaterial().equalsIgnoreCase(searchDto.getMaterial())))
                .filter(product -> searchDto.getColor() == null || searchDto.getColor().trim().isEmpty() ||
                    (product.getColor() != null && product.getColor().equalsIgnoreCase(searchDto.getColor())))
                .collect(Collectors.toList());
            
            System.out.println("Filtered products count: " + filteredProducts.size());
            
            // Debug: Print first few products
            if (!filteredProducts.isEmpty()) {
                System.out.println("First filtered product: " + filteredProducts.get(0).getName());
            } else {
                System.out.println("No products passed filtering");
                // Debug each filter step
                long activeCount = allProducts.stream()
                    .filter(product -> {
                        if (searchDto.getIsActive() != null) {
                            return product.getIsActive() != null && product.getIsActive().equals(searchDto.getIsActive());
                        }
                        return true;
                    }).count();
                System.out.println("Products after isActive filter: " + activeCount);
            }
            
            // Apply sorting
            if (searchDto.getSortBy() != null) {
                String sortOrder = searchDto.getSortOrder() != null ? searchDto.getSortOrder() : "desc";
                switch (searchDto.getSortBy()) {
                    case "price":
                        if ("asc".equalsIgnoreCase(sortOrder)) {
                            filteredProducts.sort((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
                        } else {
                            filteredProducts.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
                        }
                        break;
                    case "createdAt":
                        if ("asc".equalsIgnoreCase(sortOrder)) {
                            filteredProducts.sort((p1, p2) -> p1.getCreatedAt().compareTo(p2.getCreatedAt()));
                        } else {
                            filteredProducts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                        }
                        break;
                    case "isFeatured":
                        filteredProducts.sort((p1, p2) -> Boolean.compare(
                            p2.getIsFeatured() != null ? p2.getIsFeatured() : false, 
                            p1.getIsFeatured() != null ? p1.getIsFeatured() : false
                        ));
                        break;
                }
            }
            
            // Apply pagination
            int page = searchDto.getPage() != null ? searchDto.getPage() : 0;
            int size = searchDto.getSize() != null ? searchDto.getSize() : 12;
            int start = page * size;
            int end = Math.min(start + size, filteredProducts.size());
            
            List<Product> pagedProducts = start < filteredProducts.size() ? 
                filteredProducts.subList(start, end) : new ArrayList<>();
        
        // Convert to DTOs
            List<ProductDto> productDtos = pagedProducts.stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
        
            // Calculate pagination info
            int totalPages = (int) Math.ceil((double) filteredProducts.size() / size);
            boolean hasNext = (page + 1) < totalPages;
            boolean hasPrevious = page > 0;
        
        return new SearchResultDto<>(
                productDtos,
                (long) filteredProducts.size(),
                totalPages,
                page,
                size,
                hasNext,
                hasPrevious,
                (long) filteredProducts.size(),
                "0ms"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new SearchResultDto<>(
                new ArrayList<>(),
                0L,
                0,
                0,
                12,
                false,
                false,
                0L,
                "0ms"
            );
        }
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
