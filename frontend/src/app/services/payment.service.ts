import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CreatePaymentLinkRequest {
  productName: string;
  description: string;
  returnUrl: string;
  cancelUrl: string;
  price: number;
}

export interface PaymentLinkResponse {
  error: number;
  message: string;
  data: {
    bin: string;
    accountNumber: string;
    accountName: string;
    amount: number;
    description: string;
    orderCode: number;
    currency: string;
    paymentLinkId: string;
    status: string;
    checkoutUrl: string;
    qrCode: string;
  };
}

export interface PaymentLinkInfo {
  id: string;
  orderCode: number;
  amount: number;
  amountPaid: number;
  amountRemaining: number;
  status: string;
  createdAt: string;
  transactions: any[];
  cancellationReason?: string;
  canceledAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${environment.apiUrl}/payment`;

  constructor(private http: HttpClient) { }

  /**
   * Tạo payment link trực tiếp (không cần đơn hàng)
   */
  createPaymentLink(request: CreatePaymentLinkRequest): Observable<PaymentLinkResponse> {
    return this.http.post<PaymentLinkResponse>(`${this.apiUrl}/create-payment-link`, request);
  }

  /**
   * Tạo đơn hàng mới
   */
  createOrder(orderData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-order`, orderData);
  }

  /**
   * Tạo payment cho đơn hàng đã có
   */
  createPaymentForOrder(orderId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/create-payment/${orderId}`, {});
  }

  /**
   * Lấy thông tin payment link
   */
  getPaymentLinkInfo(orderCode: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/payment-link/${orderCode}`);
  }

  /**
   * Hủy payment link
   */
  cancelPaymentLink(orderCode: number, reason?: string): Observable<any> {
    let params: any = {};
    if (reason) {
      params = { reason };
    }
    return this.http.put(`${this.apiUrl}/payment-link/${orderCode}/cancel`, null, { params });
  }

  /**
   * Lấy thông tin đơn hàng theo order number
   */
  getOrderByNumber(orderNumber: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/order/${orderNumber}`);
  }

  /**
   * Lấy danh sách đơn hàng của user
   */
  getUserOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/orders`);
  }

  /**
   * Cập nhật trạng thái đơn hàng
   */
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/order/${orderId}/status`, null, { 
      params: { status } 
    });
  }

  /**
   * Redirect đến PayOS checkout
   */
  redirectToPayOS(checkoutUrl: string): void {
    window.location.href = checkoutUrl;
  }

  /**
   * Tạo payment link và redirect ngay lập tức
   */
  createAndRedirectToPayment(request: CreatePaymentLinkRequest): void {
    this.createPaymentLink(request).subscribe({
      next: (response) => {
        if (response.error === 0) {
          this.redirectToPayOS(response.data.checkoutUrl);
        } else {
          console.error('Payment creation failed:', response.message);
          alert('Tạo link thanh toán thất bại: ' + response.message);
        }
      },
      error: (error) => {
        console.error('Payment creation error:', error);
        alert('Có lỗi xảy ra khi tạo link thanh toán');
      }
    });
  }
}