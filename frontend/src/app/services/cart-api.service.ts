import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ApiCartItem {
  id: number;
  cartId: number;
  productId: number;
  productName: string;
  productSku: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  subtotal: number;
  sizeValue?: string;
  colorValue?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CartResponse {
  id: number;
  userId: number;
  totalAmount: number;
  itemCount: number;
  createdAt: string;
  updatedAt: string;
  items: ApiCartItem[];
}

export interface AddToCartRequest {
  productId: number;
  quantity: number;
  sizeValue?: string;
  colorValue?: string;
}

export interface UpdateCartItemRequest {
  quantity: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

@Injectable({
  providedIn: 'root'
})
export class CartApiService {
  private readonly API_URL = `${environment.apiUrl}/cart`;

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // GET /api/cart - Lấy giỏ hàng của user hiện tại
  getCartItems(): Observable<ApiResponse<CartResponse>> {
    return this.http.get<ApiResponse<CartResponse>>(`${this.API_URL}`, {
      headers: this.getHeaders()
    });
  }

  // POST /api/cart/items - Thêm sản phẩm vào giỏ hàng
  addToCart(request: AddToCartRequest): Observable<ApiResponse<CartResponse>> {
    return this.http.post<ApiResponse<CartResponse>>(`${this.API_URL}/items`, request, {
      headers: this.getHeaders()
    });
  }

  // DELETE /api/cart/items/{productId} - Xóa sản phẩm khỏi giỏ hàng
  removeFromCart(productId: number, sizeValue?: string, colorValue?: string): Observable<ApiResponse<CartResponse>> {
    let url = `${this.API_URL}/items/${productId}`;
    const params = new URLSearchParams();
    
    if (sizeValue) params.append('sizeValue', sizeValue);
    if (colorValue) params.append('colorValue', colorValue);
    
    if (params.toString()) {
      url += `?${params.toString()}`;
    }
    
    return this.http.delete<ApiResponse<CartResponse>>(url, {
      headers: this.getHeaders()
    });
  }

  // PUT /api/cart/items/{productId} - Cập nhật số lượng sản phẩm
  updateCartItem(productId: number, request: UpdateCartItemRequest, sizeValue?: string, colorValue?: string): Observable<ApiResponse<CartResponse>> {
    let url = `${this.API_URL}/items/${productId}`;
    const params = new URLSearchParams();
    
    if (sizeValue) params.append('sizeValue', sizeValue);
    if (colorValue) params.append('colorValue', colorValue);
    
    if (params.toString()) {
      url += `?${params.toString()}`;
    }
    
    return this.http.put<ApiResponse<CartResponse>>(url, request, {
      headers: this.getHeaders()
    });
  }

  // DELETE /api/cart - Xóa tất cả sản phẩm khỏi giỏ hàng
  clearCart(): Observable<ApiResponse<CartResponse>> {
    return this.http.delete<ApiResponse<CartResponse>>(`${this.API_URL}`, {
      headers: this.getHeaders()
    });
  }

  // POST /api/cart/sync - Đồng bộ giỏ hàng từ localStorage
  syncCartFromLocalStorage(localCartItems: any[]): Observable<ApiResponse<CartResponse>> {
    return this.http.post<ApiResponse<CartResponse>>(`${this.API_URL}/sync`, {
      items: localCartItems
    }, {
      headers: this.getHeaders()
    });
  }
}
