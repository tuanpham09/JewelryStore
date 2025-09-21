package org.example.trangsuc_js.service;

import org.example.trangsuc_js.dto.product.ProductDto;
import org.example.trangsuc_js.dto.search.ProductSearchDto;
import org.example.trangsuc_js.dto.search.SearchResultDto;

public interface SearchService {
    SearchResultDto<ProductDto> searchProducts(ProductSearchDto searchDto);
    SearchResultDto<ProductDto> getFeaturedProducts(int page, int size);
    SearchResultDto<ProductDto> getNewProducts(int page, int size);
    SearchResultDto<ProductDto> getBestsellerProducts(int page, int size);
    SearchResultDto<ProductDto> getOnSaleProducts(int page, int size);
}
