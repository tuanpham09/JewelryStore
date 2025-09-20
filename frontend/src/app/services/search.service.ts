import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ProductSearchDto {
  keyword?: string;
  categoryIds?: number[];
  minPrice?: number;
  maxPrice?: number;
  materials?: string[];
  colors?: string[];
  brands?: string[];
  isActive?: boolean;
  isFeatured?: boolean;
  isNew?: boolean;
  isBestseller?: boolean;
  isOnSale?: boolean;
  sortBy?: string;
  page?: number;
  size?: number;
}

export interface SearchResultDto<T> {
  data: T[];
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
