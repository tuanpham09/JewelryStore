package org.example.trangsuc_js.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.admin.*;
import org.example.trangsuc_js.dto.cart.CartDto;
import org.example.trangsuc_js.dto.cart.CartItemDto;
import org.example.trangsuc_js.dto.category.CategoryDto;
import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.product.ProductImageDto;
import org.example.trangsuc_js.dto.product.ProductUpsertDto;
import org.example.trangsuc_js.dto.report.ReportDto;
import org.example.trangsuc_js.dto.review.ReviewDto;
import org.example.trangsuc_js.entity.*;
import org.example.trangsuc_js.repository.*;
import org.example.trangsuc_js.service.AdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final ProductRepository productRepo;
    private final ProductImageRepository productImageRepo;
    private final CategoryRepository categoryRepo;
    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PromotionRepository promotionRepo;
    private final ShippingRepository shippingRepo;
    private final ReviewRepository reviewRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    // ---------------- Product -----------------
    @Override
    @Transactional
    public ProductDto createProduct(ProductUpsertDto dto) {
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        Product product = new Product();
        product.setName(dto.getName());
        product.setSlug(dto.getSlug());
        product.setDescription(dto.getDescription());
        product.setShortDescription(dto.getShortDescription());
        product.setPrice(dto.getPrice());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setStock(dto.getStock());
        product.setMinStock(dto.getMinStock());
        product.setMaxStock(dto.getMaxStock());
        product.setSku(dto.getSku());
        product.setBarcode(dto.getBarcode());
        product.setWeight(dto.getWeight());
        product.setDimensions(dto.getDimensions());
        product.setMaterial(dto.getMaterial());
        product.setColor(dto.getColor());
        product.setBrand(dto.getBrand());
        product.setOrigin(dto.getOrigin());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
        product.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        product.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false);
        product.setIsNew(dto.getIsNew() != null ? dto.getIsNew() : false);
        product.setIsBestseller(dto.getIsBestseller() != null ? dto.getIsBestseller() : false);
        product.setMetaTitle(dto.getMetaTitle());
        product.setMetaDescription(dto.getMetaDescription());
        product.setMetaKeywords(dto.getMetaKeywords());
        product.setThumbnail(dto.getThumbnail());
        product.setCategory(category);
        
        productRepo.save(product);
        return toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductUpsertDto dto) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        product.setName(dto.getName());
        product.setSlug(dto.getSlug());
        product.setDescription(dto.getDescription());
        product.setShortDescription(dto.getShortDescription());
        product.setPrice(dto.getPrice());
        product.setOriginalPrice(dto.getOriginalPrice());
        product.setSalePrice(dto.getSalePrice());
        product.setStock(dto.getStock());
        product.setMinStock(dto.getMinStock());
        product.setMaxStock(dto.getMaxStock());
        product.setSku(dto.getSku());
        product.setBarcode(dto.getBarcode());
        product.setWeight(dto.getWeight());
        product.setDimensions(dto.getDimensions());
        product.setMaterial(dto.getMaterial());
        product.setColor(dto.getColor());
        product.setBrand(dto.getBrand());
        product.setOrigin(dto.getOrigin());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
        product.setIsActive(dto.getIsActive());
        product.setIsFeatured(dto.getIsFeatured());
        product.setIsNew(dto.getIsNew());
        product.setIsBestseller(dto.getIsBestseller());
        product.setMetaTitle(dto.getMetaTitle());
        product.setMetaDescription(dto.getMetaDescription());
        product.setMetaKeywords(dto.getMetaKeywords());
        product.setThumbnail(dto.getThumbnail());
        product.setCategory(category);
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepo.save(product);
        return toDto(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private ProductDto toDto(Product p) {
        ProductDto dto = new ProductDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setSlug(p.getSlug());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setThumbnail(p.getThumbnail());
        dto.setCategoryName(p.getCategory() != null ? p.getCategory().getName() : null);
        
        // Tính average rating và review count
        if (p.getReviews() != null && !p.getReviews().isEmpty()) {
            double avgRating = p.getReviews().stream()
                    .mapToInt(r -> r.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
            dto.setReviewCount(p.getReviews().size());
        }
        
        return dto;
    }

    // ---------------- Category -----------------
    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        
        categoryRepo.save(category);
        return toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::toCategoryDto)
                .collect(Collectors.toList());
    }

    // ---------------- Orders -----------------
    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderDto> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable).getContent().stream()
                .map(this::toAdminOrderDto)
                .collect(Collectors.toList());
    }

    // ---------------- Reports -----------------
    @Override
    public ReportDto getRevenueReport() {
        List<Order> orders = orderRepo.findAll();

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = orders.size();

        long totalProductsSold = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToLong(OrderItem::getQuantity)
                .sum();

        return new ReportDto(totalRevenue, totalOrders, totalProductsSold);
    }

    // ========== NEW METHODS - PLACEHOLDER IMPLEMENTATIONS ==========
    
    @Override
    @Transactional
    public ProductDto uploadProductImage(Long productId, MultipartFile file) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        try {
            // For now, we'll just simulate file upload by creating a URL
            // In production, you would save the file to storage (AWS S3, local filesystem, etc.)
            String fileName = file.getOriginalFilename();
            String imageUrl = "/uploads/products/" + productId + "/" + fileName;
            
            // Create ProductImage entity
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setImageUrl(imageUrl);
            productImage.setAltText(fileName);
            productImage.setIsPrimary(product.getImages().isEmpty()); // First image is primary
            productImage.setSortOrder(product.getImages().size());
            
            productImageRepo.save(productImage);
            product.getImages().add(productImage);
            productRepo.save(product);
            
            return toDto(product);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteProductImage(Long productId, String imageUrl) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        ProductImage imageToDelete = product.getImages().stream()
                .filter(img -> img.getImageUrl().equals(imageUrl))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        product.getImages().remove(imageToDelete);
        productImageRepo.delete(imageToDelete);
        productRepo.save(product);
    }

    // ========== PRODUCT IMAGE MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public List<ProductImageDto> getProductImages(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        return product.getImages().stream()
                .map(this::toImageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductImageDto addProductImage(Long productId, String imageUrl, String altText) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(imageUrl);
        image.setAltText(altText);
        image.setIsPrimary(false);
        image.setSortOrder(product.getImages().size());
        
        productImageRepo.save(image);
        product.getImages().add(image);
        productRepo.save(product);
        
        return toImageDto(image);
    }

    @Override
    @Transactional
    public ProductImageDto updateProductImage(Long imageId, String altText, Integer sortOrder) {
        ProductImage image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        if (altText != null) {
            image.setAltText(altText);
        }
        if (sortOrder != null) {
            image.setSortOrder(sortOrder);
        }
        
        productImageRepo.save(image);
        return toImageDto(image);
    }

    @Override
    @Transactional
    public void deleteProductImageById(Long imageId) {
        ProductImage image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        Product product = image.getProduct();
        product.getImages().remove(image);
        productImageRepo.delete(image);
        productRepo.save(product);
    }

    @Override
    @Transactional
    public ProductImageDto setPrimaryImage(Long productId, Long imageId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Set all images to non-primary first
        product.getImages().forEach(img -> img.setIsPrimary(false));
        
        // Set the selected image as primary
        ProductImage primaryImage = product.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Image not found"));
        
        primaryImage.setIsPrimary(true);
        productImageRepo.save(primaryImage);
        productRepo.save(product);
        
        return toImageDto(primaryImage);
    }

    @Override
    @Transactional
    public ProductImageDto reorderProductImages(Long productId, List<Long> imageIds) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            ProductImage image = product.getImages().stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Image not found"));
            
            image.setSortOrder(i);
            productImageRepo.save(image);
        }
        
        productRepo.save(product);
        return new ProductImageDto(); // Return empty DTO as placeholder
    }

    private ProductImageDto toImageDto(ProductImage image) {
        ProductImageDto dto = new ProductImageDto();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setAltText(image.getAltText());
        dto.setIsPrimary(image.getIsPrimary());
        dto.setSortOrder(image.getSortOrder());
        dto.setCreatedAt(image.getCreatedAt());
        dto.setUpdatedAt(image.getUpdatedAt());
        return dto;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        category.setIsActive(dto.getIsActive());
        category.setSortOrder(dto.getSortOrder());
        category.setUpdatedAt(LocalDateTime.now());
        
        categoryRepo.save(category);
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
        dto.setProductCount((long) category.getProducts().size());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserDto> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable).getContent().stream()
                .map(this::toAdminUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toAdminUserDto(user);
    }

    @Override
    @Transactional
    public AdminUserDto updateUserStatus(Long id, boolean enabled) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEnabled(enabled);
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
        
        return toAdminUserDto(user);
    }

    @Override
    @Transactional
    public AdminUserDto updateUserRoles(Long id, List<String> roles) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Clear existing roles
        user.getRoles().clear();
        
        // Add new roles
        for (String roleName : roles) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.addRole(role);
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepo.save(user);
        
        return toAdminUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user has orders
        if (!user.getOrders().isEmpty()) {
            throw new RuntimeException("Cannot delete user with existing orders");
        }
        
        userRepo.delete(user);
    }

    private AdminUserDto toAdminUserDto(User user) {
        AdminUserDto dto = new AdminUserDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setGender(user.getGender() != null ? user.getGender().name() : null);
        dto.setEnabled(user.getEnabled());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setPhoneVerified(user.getPhoneVerified());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setProvince(user.getProvince());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());
        dto.setPreferredLanguage(user.getPreferredLanguage());
        dto.setPreferredCurrency(user.getPreferredCurrency());
        
        // Calculate total orders and spent
        dto.setTotalOrders(user.getOrders().size());
        dto.setTotalSpent(user.getOrders().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus().name()))
                .mapToDouble(o -> o.getTotal().doubleValue())
                .sum());
        
        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public AdminOrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toAdminOrderDto(order);
    }

    @Override
    @Transactional
    public AdminOrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            
            if (newStatus == Order.OrderStatus.DELIVERED) {
                order.setDeliveredAt(LocalDateTime.now());
            }
            
            orderRepo.save(order);
            return toAdminOrderDto(order);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    @Override
    @Transactional
    public AdminOrderDto cancelOrder(Long id, String reason) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == Order.OrderStatus.DELIVERED || order.getStatus() == Order.OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot cancel order with status: " + order.getStatus());
        }
        
        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancellationReason(reason);
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepo.save(order);
        return toAdminOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderDto> getOrdersByStatus(String status, Pageable pageable) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            return orderRepo.findByStatus(orderStatus, pageable).getContent().stream()
                    .map(this::toAdminOrderDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    private AdminOrderDto toAdminOrderDto(Order order) {
        AdminOrderDto dto = new AdminOrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus().name());
        dto.setTotal(order.getTotal());
        dto.setSubtotal(order.getSubtotal());
        dto.setShippingFee(order.getShippingFee());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setDeliveredAt(order.getDeliveredAt());
        dto.setCancelledAt(order.getCancelledAt());
        dto.setCancellationReason(order.getCancellationReason());
        
        // Customer info
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setCustomerPhone(order.getCustomerPhone());
        
        // Address info
        dto.setShippingAddress(order.getShippingAddress());
        dto.setBillingAddress(order.getBillingAddress());
        
        // Payment info
        dto.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentReference(order.getPaymentReference());
        dto.setPaidAt(order.getPaidAt());
        
        // Shipping info
        dto.setShippingMethod(null); // Order doesn't have shipping method directly
        dto.setTrackingNumber(null); // Order doesn't have tracking number directly
        
        // Order items
        dto.setItems(order.getItems().stream()
                .map(this::toAdminOrderItemDto)
                .collect(Collectors.toList()));
        
        // Notes
        dto.setNotes(order.getNotes());
        
        return dto;
    }

    private AdminOrderItemDto toAdminOrderItemDto(OrderItem item) {
        AdminOrderItemDto dto = new AdminOrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProductName());
        dto.setProductSku(item.getProductSku());
        dto.setProductThumbnail(item.getProduct().getThumbnail());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getPrice());
        dto.setPrice(item.getPrice());
        dto.setDiscountAmount(item.getDiscountAmount());
        dto.setSubtotal(item.getSubtotal());
        dto.setCreatedAt(item.getCreatedAt());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getInventoryStatus() {
        return productRepo.findAll().stream()
                .map(this::toInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getLowStockProducts() {
        return productRepo.findAll().stream()
                .filter(Product::isLowStock)
                .map(this::toInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryDto> getOutOfStockProducts() {
        return productRepo.findAll().stream()
                .filter(Product::isOutOfStock)
                .map(this::toInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryDto updateProductStock(Long productId, Integer newStock) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
        
        return toInventoryDto(product);
    }

    @Override
    @Transactional
    public InventoryDto setMinMaxStock(Long productId, Integer minStock, Integer maxStock) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setMinStock(minStock);
        product.setMaxStock(maxStock);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);
        
        return toInventoryDto(product);
    }

    private InventoryDto toInventoryDto(Product product) {
        InventoryDto dto = new InventoryDto();
        dto.setProductId(product.getId());
        dto.setProductName(product.getName());
        dto.setProductThumbnail(product.getThumbnail());
        dto.setCurrentStock(product.getStock());
        dto.setMinStock(product.getMinStock());
        dto.setMaxStock(product.getMaxStock());
        dto.setStatus(product.isOutOfStock() ? "OUT_OF_STOCK" : 
                     product.isLowStock() ? "LOW_STOCK" : "IN_STOCK");
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

    @Override
    @Transactional
    public PromotionDto createPromotion(PromotionDto dto) {
        Promotion promotion = new Promotion();
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setType(Promotion.PromotionType.valueOf(dto.getType()));
        promotion.setValue(dto.getValue());
        promotion.setMinOrderAmount(dto.getMinOrderAmount());
        promotion.setCode(dto.getCode());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setActive(dto.isActive());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setUsedCount(dto.getUsedCount());
        promotion.setApplicableProducts(Promotion.ApplicableProducts.valueOf(dto.getApplicableProducts()));
        promotion.setApplicableCategories(dto.getApplicableCategories());
        promotion.setApplicableProductIds(dto.getApplicableProductIds());
        
        promotionRepo.save(promotion);
        return toPromotionDto(promotion);
    }

    @Override
    @Transactional
    public PromotionDto updatePromotion(Long id, PromotionDto dto) {
        Promotion promotion = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        
        promotion.setName(dto.getName());
        promotion.setDescription(dto.getDescription());
        promotion.setType(Promotion.PromotionType.valueOf(dto.getType()));
        promotion.setValue(dto.getValue());
        promotion.setMinOrderAmount(dto.getMinOrderAmount());
        promotion.setCode(dto.getCode());
        promotion.setStartDate(dto.getStartDate());
        promotion.setEndDate(dto.getEndDate());
        promotion.setActive(dto.isActive());
        promotion.setUsageLimit(dto.getUsageLimit());
        promotion.setUsedCount(dto.getUsedCount());
        promotion.setApplicableProducts(Promotion.ApplicableProducts.valueOf(dto.getApplicableProducts()));
        promotion.setApplicableCategories(dto.getApplicableCategories());
        promotion.setApplicableProductIds(dto.getApplicableProductIds());
        promotion.setUpdatedAt(LocalDateTime.now());
        
        promotionRepo.save(promotion);
        return toPromotionDto(promotion);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        
        promotionRepo.delete(promotion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionDto> getAllPromotions() {
        return promotionRepo.findAll().stream()
                .map(this::toPromotionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PromotionDto togglePromotionStatus(Long id) {
        Promotion promotion = promotionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found"));
        
        promotion.setActive(!promotion.isActive());
        promotion.setUpdatedAt(LocalDateTime.now());
        promotionRepo.save(promotion);
        
        return toPromotionDto(promotion);
    }

    private PromotionDto toPromotionDto(Promotion promotion) {
        PromotionDto dto = new PromotionDto();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDescription(promotion.getDescription());
        dto.setType(promotion.getType().name());
        dto.setValue(promotion.getValue());
        dto.setMinOrderAmount(promotion.getMinOrderAmount());
        dto.setCode(promotion.getCode());
        dto.setStartDate(promotion.getStartDate());
        dto.setEndDate(promotion.getEndDate());
        dto.setActive(promotion.isActive());
        dto.setUsageLimit(promotion.getUsageLimit());
        dto.setUsedCount(promotion.getUsedCount());
        dto.setApplicableProducts(promotion.getApplicableProducts().name());
        dto.setApplicableCategories(promotion.getApplicableCategories());
        dto.setApplicableProductIds(promotion.getApplicableProductIds());
        return dto;
    }

    // ========== SHIPPING MANAGEMENT ==========
    @Override
    @Transactional(readOnly = true)
    public List<ShippingDto> getAllShippings(Pageable pageable) {
        return shippingRepo.findAll(pageable).getContent().stream()
                .map(this::toShippingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingDto getShippingById(Long id) {
        Shipping shipping = shippingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        return toShippingDto(shipping);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingDto getShippingByOrderId(Long orderId) {
        Shipping shipping = shippingRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Shipping not found for order: " + orderId));
        return toShippingDto(shipping);
    }

    @Override
    @Transactional
    public ShippingDto createShipping(ShippingDto dto) {
        Order order = orderRepo.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        Shipping shipping = new Shipping();
        shipping.setOrder(order);
        shipping.setShippingMethod(Shipping.ShippingMethod.valueOf(dto.getShippingMethod()));
        shipping.setTrackingNumber(dto.getTrackingNumber());
        shipping.setStatus(Shipping.ShippingStatus.valueOf(dto.getStatus()));
        shipping.setFromAddress(dto.getFromAddress());
        shipping.setToAddress(dto.getToAddress());
        shipping.setCustomerName(dto.getCustomerName());
        shipping.setCustomerPhone(dto.getCustomerPhone());
        shipping.setShippingFee(dto.getShippingFee());
        shipping.setEstimatedDelivery(dto.getEstimatedDelivery());
        shipping.setActualDelivery(dto.getActualDelivery());
        shipping.setNotes(dto.getNotes());
        
        shippingRepo.save(shipping);
        return toShippingDto(shipping);
    }

    @Override
    @Transactional
    public ShippingDto updateShippingStatus(Long id, String status) {
        Shipping shipping = shippingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        
        try {
            Shipping.ShippingStatus newStatus = Shipping.ShippingStatus.valueOf(status.toUpperCase());
            shipping.setStatus(newStatus);
            shipping.setUpdatedAt(LocalDateTime.now());
            
            if (newStatus == Shipping.ShippingStatus.DELIVERED) {
                shipping.setActualDelivery(LocalDateTime.now());
            }
            
            shippingRepo.save(shipping);
            return toShippingDto(shipping);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid shipping status: " + status);
        }
    }

    @Override
    @Transactional
    public ShippingDto updateTrackingNumber(Long id, String trackingNumber) {
        Shipping shipping = shippingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        
        shipping.setTrackingNumber(trackingNumber);
        shipping.setUpdatedAt(LocalDateTime.now());
        shippingRepo.save(shipping);
        
        return toShippingDto(shipping);
    }


    private ShippingDto toShippingDto(Shipping shipping) {
        ShippingDto dto = new ShippingDto();
        dto.setId(shipping.getId());
        dto.setOrderId(shipping.getOrder().getId());
        dto.setShippingMethod(shipping.getShippingMethod().name());
        dto.setTrackingNumber(shipping.getTrackingNumber());
        dto.setStatus(shipping.getStatus().name());
        dto.setFromAddress(shipping.getFromAddress());
        dto.setToAddress(shipping.getToAddress());
        dto.setCustomerName(shipping.getCustomerName());
        dto.setCustomerPhone(shipping.getCustomerPhone());
        dto.setShippingFee(shipping.getShippingFee());
        dto.setEstimatedDelivery(shipping.getEstimatedDelivery());
        dto.setActualDelivery(shipping.getActualDelivery());
        dto.setCreatedAt(shipping.getCreatedAt());
        dto.setUpdatedAt(shipping.getUpdatedAt());
        dto.setNotes(shipping.getNotes());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        
        // Total revenue
        BigDecimal totalRevenue = orderRepo.findAll().stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.setTotalRevenue(totalRevenue);
        
        // Total orders
        long totalOrders = orderRepo.count();
        stats.setTotalOrders(totalOrders);
        
        // Total customers
        long totalCustomers = userRepo.count();
        stats.setTotalCustomers(totalCustomers);
        
        // Total products
        long totalProducts = productRepo.count();
        stats.setTotalProducts(totalProducts);
        
        // Pending orders
        long pendingOrders = orderRepo.findByStatus(Order.OrderStatus.PENDING).size();
        stats.setPendingOrders(pendingOrders);
        
        // Low stock products
        long lowStockProducts = productRepo.findAll().stream()
                .filter(Product::isLowStock)
                .count();
        stats.setLowStockProducts(lowStockProducts);
        
        // Out of stock products
        long outOfStockProducts = productRepo.findAll().stream()
                .filter(Product::isOutOfStock)
                .count();
        stats.setOutOfStockProducts(outOfStockProducts);
        
        // Active promotions
        long activePromotions = promotionRepo.findAll().stream()
                .filter(Promotion::isActive)
                .count();
        stats.setActivePromotions(activePromotions);
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDto getRevenueReportByDateRange(String startDate, String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
        
        List<Order> orders = orderRepo.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(start) && o.getCreatedAt().isBefore(end))
                .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                .collect(Collectors.toList());
        
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long totalOrders = orders.size();
        
        long totalProductsSold = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .mapToLong(OrderItem::getQuantity)
                .sum();
        
        return new ReportDto(totalRevenue, totalOrders, totalProductsSold);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getTopSellingProducts(int limit) {
        return productRepo.findAll().stream()
                .sorted((p1, p2) -> Long.compare(p2.getSoldCount(), p1.getSoldCount()))
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserDto> getTopCustomers(int limit) {
        return userRepo.findAll().stream()
                .sorted((u1, u2) -> {
                    double spent1 = u1.getOrders().stream()
                            .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                            .mapToDouble(o -> o.getTotal().doubleValue())
                            .sum();
                    double spent2 = u2.getOrders().stream()
                            .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                            .mapToDouble(o -> o.getTotal().doubleValue())
                            .sum();
                    return Double.compare(spent2, spent1);
                })
                .limit(limit)
                .map(this::toAdminUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] exportOrdersToExcel(String startDate, String endDate) {
        // TODO: Implement export orders to Excel using Apache POI
        // For now, return a placeholder
        return "Orders export data".getBytes();
    }

    @Override
    public byte[] exportProductsToExcel() {
        // TODO: Implement export products to Excel using Apache POI
        // For now, return a placeholder
        return "Products export data".getBytes();
    }

    @Override
    public byte[] exportCustomersToExcel() {
        // TODO: Implement export customers to Excel using Apache POI
        // For now, return a placeholder
        return "Customers export data".getBytes();
    }

    // ========== REVIEW MANAGEMENT ==========
    
    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getAllReviews(Pageable pageable) {
        return reviewRepo.findAll(pageable).getContent().stream()
                .map(this::toReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDto approveReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        review.setIsApproved(true);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepo.save(review);
        
        return toReviewDto(review);
    }

    @Override
    @Transactional
    public ReviewDto rejectReview(Long reviewId, String reason) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        review.setIsApproved(false);
        review.setAdminComment(reason);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepo.save(review);
        
        return toReviewDto(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        reviewRepo.delete(review);
    }

    private ReviewDto toReviewDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getFullName());
        dto.setUserAvatar(review.getUser().getAvatarUrl());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        dto.setIsVerifiedPurchase(review.getIsVerifiedPurchase());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setIsApproved(review.getIsApproved());
        dto.setAdminComment(review.getAdminComment());
        return dto;
    }



    @Override
    @Transactional(readOnly = true)
    public List<ShippingDto> getShippingsByStatus(String status, Pageable pageable) {
        try {
            Shipping.ShippingStatus shippingStatus = Shipping.ShippingStatus.valueOf(status.toUpperCase());
            return shippingRepo.findByStatus(shippingStatus, pageable).getContent().stream()
                    .map(this::toShippingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid shipping status: " + status);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingDto> getShippingsByMethod(String method, Pageable pageable) {
        try {
            Shipping.ShippingMethod shippingMethod = Shipping.ShippingMethod.valueOf(method.toUpperCase());
            return shippingRepo.findByShippingMethod(shippingMethod, pageable).getContent().stream()
                    .map(this::toShippingDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid shipping method: " + method);
        }
    }
}
