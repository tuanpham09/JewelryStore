package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.search.ProductSearchDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;
import org.example.trangsuc_js.entity.Product;
import org.example.trangsuc_js.repository.ProductRepository;
import org.example.trangsuc_js.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    
    private final ProductRepository productRepo;
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> searchProducts(ProductSearchDto searchDto) {
        long startTime = System.currentTimeMillis();
        
        // Create pageable with sorting
        Pageable pageable = createPageable(searchDto);
        
        // Get products based on search criteria
        Page<Product> productPage = getProductsBySearchCriteria(searchDto, pageable);
        
        // Convert to DTOs
        List<ProductDto> productDtos = productPage.getContent().stream()
                .map(this::toProductDto)
                .collect(Collectors.toList());
        
        long endTime = System.currentTimeMillis();
        String searchTime = (endTime - startTime) + "ms";
        
        return new SearchResultDto<>(
                productDtos,
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.hasNext(),
                productPage.hasPrevious(),
                productPage.getTotalElements(),
                searchTime
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getFeaturedProducts(int page, int size) {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setIsFeatured(true);
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy("popularity_desc");
        return searchProducts(searchDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getNewProducts(int page, int size) {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setIsNew(true);
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy("newest");
        return searchProducts(searchDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getBestsellerProducts(int page, int size) {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setIsBestseller(true);
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy("popularity_desc");
        return searchProducts(searchDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SearchResultDto<ProductDto> getOnSaleProducts(int page, int size) {
        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setIsOnSale(true);
        searchDto.setPage(page);
        searchDto.setSize(size);
        searchDto.setSortBy("price_asc");
        return searchProducts(searchDto);
    }
    
    private Pageable createPageable(ProductSearchDto searchDto) {
        Sort sort = createSort(searchDto.getSortBy());
        return PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);
    }
    
    private Sort createSort(String sortBy) {
        if (sortBy == null) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        
        switch (sortBy) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "rating_desc":
                return Sort.by(Sort.Direction.DESC, "averageRating");
            case "popularity_desc":
                return Sort.by(Sort.Direction.DESC, "soldCount");
            case "newest":
                return Sort.by(Sort.Direction.DESC, "createdAt");
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }
    }
    
    private Page<Product> getProductsBySearchCriteria(ProductSearchDto searchDto, Pageable pageable) {
        // For now, we'll use a simple approach with findAll and filter in memory
        // In production, you would use JPA Criteria API or custom queries
        
        List<Product> allProducts = productRepo.findAllWithReviews();
        
        // Apply filters
        List<Product> filteredProducts = allProducts.stream()
                .filter(product -> searchDto.getIsActive() == null || product.getIsActive().equals(searchDto.getIsActive()))
                .filter(product -> searchDto.getIsFeatured() == null || product.getIsFeatured().equals(searchDto.getIsFeatured()))
                .filter(product -> searchDto.getIsNew() == null || product.getIsNew().equals(searchDto.getIsNew()))
                .filter(product -> searchDto.getIsBestseller() == null || product.getIsBestseller().equals(searchDto.getIsBestseller()))
                .filter(product -> searchDto.getIsOnSale() == null || product.isOnSale() == searchDto.getIsOnSale())
                .filter(product -> searchDto.getKeyword() == null || 
                        product.getName().toLowerCase().contains(searchDto.getKeyword().toLowerCase()) ||
                        (product.getDescription() != null && product.getDescription().toLowerCase().contains(searchDto.getKeyword().toLowerCase())))
                .filter(product -> searchDto.getMinPrice() == null || product.getPrice().compareTo(searchDto.getMinPrice()) >= 0)
                .filter(product -> searchDto.getMaxPrice() == null || product.getPrice().compareTo(searchDto.getMaxPrice()) <= 0)
                .filter(product -> searchDto.getCategoryIds() == null || searchDto.getCategoryIds().isEmpty() || 
                        (product.getCategory() != null && searchDto.getCategoryIds().contains(product.getCategory().getId())))
                .filter(product -> searchDto.getMaterials() == null || searchDto.getMaterials().isEmpty() || 
                        (product.getMaterial() != null && searchDto.getMaterials().contains(product.getMaterial())))
                .filter(product -> searchDto.getColors() == null || searchDto.getColors().isEmpty() || 
                        (product.getColor() != null && searchDto.getColors().contains(product.getColor())))
                .filter(product -> searchDto.getBrands() == null || searchDto.getBrands().isEmpty() || 
                        (product.getBrand() != null && searchDto.getBrands().contains(product.getBrand())))
                .collect(Collectors.toList());
        
        // Apply sorting
        if (searchDto.getSortBy() != null) {
            switch (searchDto.getSortBy()) {
                case "price_asc":
                    filteredProducts.sort((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()));
                    break;
                case "price_desc":
                    filteredProducts.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
                    break;
                case "rating_desc":
                    filteredProducts.sort((p1, p2) -> {
                        double rating1 = p1.getReviews().stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
                        double rating2 = p2.getReviews().stream().mapToInt(r -> r.getRating()).average().orElse(0.0);
                        return Double.compare(rating2, rating1);
                    });
                    break;
                case "popularity_desc":
                    filteredProducts.sort((p1, p2) -> Long.compare(p2.getSoldCount(), p1.getSoldCount()));
                    break;
                case "newest":
                    filteredProducts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                    break;
            }
        }
        
        // Apply pagination
        int start = searchDto.getPage() * searchDto.getSize();
        int end = Math.min(start + searchDto.getSize(), filteredProducts.size());
        List<Product> pagedProducts = filteredProducts.subList(start, end);
        
        // Create a mock Page object
        return new Page<Product>() {
            @Override
            public int getNumber() {
                return searchDto.getPage();
            }
            
            @Override
            public int getSize() {
                return searchDto.getSize();
            }
            
            @Override
            public int getNumberOfElements() {
                return pagedProducts.size();
            }
            
            @Override
            public List<Product> getContent() {
                return pagedProducts;
            }
            
            @Override
            public boolean hasContent() {
                return !pagedProducts.isEmpty();
            }
            
            @Override
            public Sort getSort() {
                return createSort(searchDto.getSortBy());
            }
            
            @Override
            public boolean isFirst() {
                return searchDto.getPage() == 0;
            }
            
            @Override
            public boolean isLast() {
                return end >= filteredProducts.size();
            }
            
            @Override
            public boolean hasNext() {
                return end < filteredProducts.size();
            }
            
            @Override
            public boolean hasPrevious() {
                return searchDto.getPage() > 0;
            }
            
            @Override
            public Pageable nextPageable() {
                return hasNext() ? PageRequest.of(getNumber() + 1, getSize(), getSort()) : Pageable.unpaged();
            }
            
            @Override
            public Pageable previousPageable() {
                return hasPrevious() ? PageRequest.of(getNumber() - 1, getSize(), getSort()) : Pageable.unpaged();
            }
            
            @Override
            public int getTotalPages() {
                return (int) Math.ceil((double) filteredProducts.size() / searchDto.getSize());
            }
            
            @Override
            public long getTotalElements() {
                return filteredProducts.size();
            }
            
            @Override
            public <U> Page<U> map(java.util.function.Function<? super Product, ? extends U> converter) {
                return null;
            }
            
            @Override
            public java.util.Iterator<Product> iterator() {
                return pagedProducts.iterator();
            }
        };
    }
    
    private ProductDto toProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setPrice(product.getPrice());
        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setCurrentPrice(product.getCurrentPrice());
        dto.setStock(product.getStock());
        dto.setMinStock(product.getMinStock());
        dto.setMaxStock(product.getMaxStock());
        dto.setSku(product.getSku());
        dto.setBarcode(product.getBarcode());
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setMaterial(product.getMaterial());
        dto.setColor(product.getColor());
        dto.setBrand(product.getBrand());
        dto.setOrigin(product.getOrigin());
        dto.setWarrantyPeriod(product.getWarrantyPeriod());
        dto.setIsActive(product.getIsActive());
        dto.setIsFeatured(product.getIsFeatured());
        dto.setIsNew(product.getIsNew());
        dto.setIsBestseller(product.getIsBestseller());
        dto.setIsOnSale(product.isOnSale());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setIsLowStock(product.isLowStock());
        dto.setIsOutOfStock(product.isOutOfStock());
        dto.setViewCount(product.getViewCount());
        dto.setSoldCount(product.getSoldCount());
        dto.setMetaTitle(product.getMetaTitle());
        dto.setMetaDescription(product.getMetaDescription());
        dto.setMetaKeywords(product.getMetaKeywords());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setThumbnail(product.getThumbnail());
        dto.setPrimaryImageUrl(product.getPrimaryImageUrl());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        
        // Calculate average rating and review count
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            double avgRating = product.getReviews().stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
            dto.setReviewCount(product.getReviews().size());
        }
        
        return dto;
    }
}
