package org.example.trangsuc_js.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trangsuc_js.dto.language.LanguageDto;
import org.example.trangsuc_js.dto.language.TranslationDto;
import org.example.trangsuc_js.service.LanguageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    
    private String currentLanguage = "vi";
    
    @Override
    @Transactional(readOnly = true)
    public List<LanguageDto> getAvailableLanguages() {
        return Arrays.asList(
            new LanguageDto("vi", "Vietnamese", "Tiếng Việt", "🇻🇳", true, true),
            new LanguageDto("en", "English", "English", "🇺🇸", true, false),
            new LanguageDto("zh", "Chinese", "中文", "🇨🇳", true, false),
            new LanguageDto("ja", "Japanese", "日本語", "🇯🇵", true, false)
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public LanguageDto getCurrentLanguage() {
        return getAvailableLanguages().stream()
                .filter(lang -> lang.getCode().equals(currentLanguage))
                .findFirst()
                .orElse(getAvailableLanguages().get(0));
    }
    
    @Override
    @Transactional
    public void setCurrentLanguage(String languageCode) {
        this.currentLanguage = languageCode;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getTranslations(String languageCode) {
        Map<String, String> translations = new HashMap<>();
        
        // Common translations
        translations.putAll(getCommonTranslations(languageCode));
        translations.putAll(getProductTranslations(languageCode));
        translations.putAll(getOrderTranslations(languageCode));
        translations.putAll(getUserTranslations(languageCode));
        
        return translations;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, String> getTranslationsByCategory(String languageCode, String category) {
        switch (category.toLowerCase()) {
            case "common":
                return getCommonTranslations(languageCode);
            case "product":
                return getProductTranslations(languageCode);
            case "order":
                return getOrderTranslations(languageCode);
            case "user":
                return getUserTranslations(languageCode);
            default:
                return new HashMap<>();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public String translate(String key, String languageCode) {
        Map<String, String> translations = getTranslations(languageCode);
        return translations.getOrDefault(key, key);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String translate(String key) {
        return translate(key, currentLanguage);
    }
    
    private Map<String, String> getCommonTranslations(String languageCode) {
        Map<String, String> translations = new HashMap<>();
        
        switch (languageCode) {
            case "vi":
                translations.put("home", "Trang chủ");
                translations.put("products", "Sản phẩm");
                translations.put("cart", "Giỏ hàng");
                translations.put("orders", "Đơn hàng");
                translations.put("profile", "Hồ sơ");
                translations.put("login", "Đăng nhập");
                translations.put("register", "Đăng ký");
                translations.put("logout", "Đăng xuất");
                translations.put("search", "Tìm kiếm");
                translations.put("filter", "Lọc");
                translations.put("sort", "Sắp xếp");
                translations.put("price", "Giá");
                translations.put("add_to_cart", "Thêm vào giỏ");
                translations.put("buy_now", "Mua ngay");
                translations.put("wishlist", "Yêu thích");
                translations.put("loading", "Đang tải...");
                translations.put("error", "Lỗi");
                translations.put("success", "Thành công");
                translations.put("cancel", "Hủy");
                translations.put("confirm", "Xác nhận");
                translations.put("save", "Lưu");
                translations.put("edit", "Sửa");
                translations.put("delete", "Xóa");
                translations.put("view", "Xem");
                translations.put("back", "Quay lại");
                translations.put("next", "Tiếp theo");
                translations.put("previous", "Trước");
                break;
            case "en":
                translations.put("home", "Home");
                translations.put("products", "Products");
                translations.put("cart", "Cart");
                translations.put("orders", "Orders");
                translations.put("profile", "Profile");
                translations.put("login", "Login");
                translations.put("register", "Register");
                translations.put("logout", "Logout");
                translations.put("search", "Search");
                translations.put("filter", "Filter");
                translations.put("sort", "Sort");
                translations.put("price", "Price");
                translations.put("add_to_cart", "Add to Cart");
                translations.put("buy_now", "Buy Now");
                translations.put("wishlist", "Wishlist");
                translations.put("loading", "Loading...");
                translations.put("error", "Error");
                translations.put("success", "Success");
                translations.put("cancel", "Cancel");
                translations.put("confirm", "Confirm");
                translations.put("save", "Save");
                translations.put("edit", "Edit");
                translations.put("delete", "Delete");
                translations.put("view", "View");
                translations.put("back", "Back");
                translations.put("next", "Next");
                translations.put("previous", "Previous");
                break;
        }
        
        return translations;
    }
    
    private Map<String, String> getProductTranslations(String languageCode) {
        Map<String, String> translations = new HashMap<>();
        
        switch (languageCode) {
            case "vi":
                translations.put("product_name", "Tên sản phẩm");
                translations.put("product_description", "Mô tả sản phẩm");
                translations.put("product_price", "Giá sản phẩm");
                translations.put("product_category", "Danh mục");
                translations.put("product_rating", "Đánh giá");
                translations.put("product_reviews", "Đánh giá");
                translations.put("product_stock", "Tồn kho");
                translations.put("product_sold", "Đã bán");
                translations.put("product_new", "Mới");
                translations.put("product_featured", "Nổi bật");
                translations.put("product_bestseller", "Bán chạy");
                translations.put("product_on_sale", "Giảm giá");
                translations.put("product_out_of_stock", "Hết hàng");
                translations.put("product_low_stock", "Sắp hết hàng");
                break;
            case "en":
                translations.put("product_name", "Product Name");
                translations.put("product_description", "Product Description");
                translations.put("product_price", "Product Price");
                translations.put("product_category", "Category");
                translations.put("product_rating", "Rating");
                translations.put("product_reviews", "Reviews");
                translations.put("product_stock", "Stock");
                translations.put("product_sold", "Sold");
                translations.put("product_new", "New");
                translations.put("product_featured", "Featured");
                translations.put("product_bestseller", "Bestseller");
                translations.put("product_on_sale", "On Sale");
                translations.put("product_out_of_stock", "Out of Stock");
                translations.put("product_low_stock", "Low Stock");
                break;
        }
        
        return translations;
    }
    
    private Map<String, String> getOrderTranslations(String languageCode) {
        Map<String, String> translations = new HashMap<>();
        
        switch (languageCode) {
            case "vi":
                translations.put("order_id", "Mã đơn hàng");
                translations.put("order_status", "Trạng thái đơn hàng");
                translations.put("order_total", "Tổng tiền");
                translations.put("order_date", "Ngày đặt");
                translations.put("order_pending", "Chờ xử lý");
                translations.put("order_processing", "Đang xử lý");
                translations.put("order_shipped", "Đã gửi hàng");
                translations.put("order_delivered", "Đã giao hàng");
                translations.put("order_cancelled", "Đã hủy");
                translations.put("order_tracking", "Theo dõi đơn hàng");
                translations.put("order_history", "Lịch sử đơn hàng");
                break;
            case "en":
                translations.put("order_id", "Order ID");
                translations.put("order_status", "Order Status");
                translations.put("order_total", "Total");
                translations.put("order_date", "Order Date");
                translations.put("order_pending", "Pending");
                translations.put("order_processing", "Processing");
                translations.put("order_shipped", "Shipped");
                translations.put("order_delivered", "Delivered");
                translations.put("order_cancelled", "Cancelled");
                translations.put("order_tracking", "Order Tracking");
                translations.put("order_history", "Order History");
                break;
        }
        
        return translations;
    }
    
    private Map<String, String> getUserTranslations(String languageCode) {
        Map<String, String> translations = new HashMap<>();
        
        switch (languageCode) {
            case "vi":
                translations.put("user_profile", "Hồ sơ người dùng");
                translations.put("user_name", "Tên người dùng");
                translations.put("user_email", "Email");
                translations.put("user_phone", "Số điện thoại");
                translations.put("user_address", "Địa chỉ");
                translations.put("user_orders", "Đơn hàng của tôi");
                translations.put("user_wishlist", "Danh sách yêu thích");
                translations.put("user_reviews", "Đánh giá của tôi");
                translations.put("user_settings", "Cài đặt");
                translations.put("user_logout", "Đăng xuất");
                break;
            case "en":
                translations.put("user_profile", "User Profile");
                translations.put("user_name", "User Name");
                translations.put("user_email", "Email");
                translations.put("user_phone", "Phone");
                translations.put("user_address", "Address");
                translations.put("user_orders", "My Orders");
                translations.put("user_wishlist", "My Wishlist");
                translations.put("user_reviews", "My Reviews");
                translations.put("user_settings", "Settings");
                translations.put("user_logout", "Logout");
                break;
        }
        
        return translations;
    }
}
