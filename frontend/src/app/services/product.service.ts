import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Product {
  id: number;
  name: string;
  slug: string;
  description: string;
  price: number;
  originalPrice?: number;
  salePrice?: number;
  stock: number;
  thumbnail: string;
  categoryName: string;
  averageRating?: number;
  reviewCount?: number;
  isFeatured?: boolean;
  isNew?: boolean;
  isBestseller?: boolean;
  isOnSale?: boolean;
  brand?: string;
  material?: string;
  color?: string;
}

export interface ProductResponse {
  success: boolean;
  message: string;
  data: Product[];
}

export interface SearchResultDto<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
  hasNext: boolean;
  hasPrevious: boolean;
  totalCount: number;
  searchTime: string;
}

export interface SearchResponse {
  success: boolean;
  message: string;
  data: SearchResultDto<Product>;
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Lấy tất cả sản phẩm
  getAllProducts(): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products`);
  }

  // Lấy sản phẩm theo ID
  getProductById(id: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products/${id}`);
  }

  // Lấy sản phẩm theo danh mục
  getProductsByCategory(categoryId: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.apiUrl}/products/category/${categoryId}`);
  }

  // Lấy sản phẩm nổi bật
  getFeaturedProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/featured?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm mới
  getNewProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/new?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm bán chạy
  getBestsellerProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/bestsellers?page=${page}&size=${size}`);
  }

  // Lấy sản phẩm đang giảm giá
  getOnSaleProducts(page: number = 0, size: number = 20): Observable<SearchResponse> {
    return this.http.get<SearchResponse>(`${this.apiUrl}/search/products/on-sale?page=${page}&size=${size}`);
  }
}
