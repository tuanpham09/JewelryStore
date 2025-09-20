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

export interface ProductSearchDto {
  query?: string;
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  brand?: string;
  material?: string;
  color?: string;
  sortBy?: string;
  sortOrder?: string;
  page?: number;
  size?: number;
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

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface SearchResponse {
  success: boolean;
  message: string;
  data: SearchResultDto<Product>;
}

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private apiUrl = `${environment.apiUrl}/search`;

  constructor(private http: HttpClient) { }

  searchProducts(searchDto: ProductSearchDto): Observable<ApiResponse<SearchResultDto<any>>> {
    return this.http.post<ApiResponse<SearchResultDto<any>>>(`${this.apiUrl}/products`, searchDto);
  }

  getFeaturedProducts(page: number = 0, size: number = 20): Observable<ApiResponse<SearchResultDto<any>>> {
    return this.http.get<ApiResponse<SearchResultDto<any>>>(`${this.apiUrl}/products/featured?page=${page}&size=${size}`);
  }

  getNewProducts(page: number = 0, size: number = 20): Observable<ApiResponse<SearchResultDto<any>>> {
    return this.http.get<ApiResponse<SearchResultDto<any>>>(`${this.apiUrl}/products/new?page=${page}&size=${size}`);
  }

  getBestsellerProducts(page: number = 0, size: number = 20): Observable<ApiResponse<SearchResultDto<any>>> {
    return this.http.get<ApiResponse<SearchResultDto<any>>>(`${this.apiUrl}/products/bestsellers?page=${page}&size=${size}`);
  }

  getOnSaleProducts(page: number = 0, size: number = 20): Observable<ApiResponse<SearchResultDto<any>>> {
    return this.http.get<ApiResponse<SearchResultDto<any>>>(`${this.apiUrl}/products/on-sale?page=${page}&size=${size}`);
  }
}
