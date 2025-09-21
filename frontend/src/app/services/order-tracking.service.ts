import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface OrderTrackingDto {
  id: number;
  orderNumber: string;
  status: string;
  totalAmount: number;
  shippingName: string;
  shippingPhone: string;
  shippingAddress: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
  deliveredAt?: string;
  items: OrderItemDto[];
  trackingHistory: TrackingHistoryDto[];
  payment?: PaymentDto;
}

export interface OrderItemDto {
  id: number;
  productId: number;
  productName: string;
  productImage: string;
  quantity: number;
  unitPrice: number;
  subtotal: number;
  sizeValue?: string;
  colorValue?: string;
}

export interface TrackingHistoryDto {
  id: number;
  status: string;
  description: string;
  timestamp: string;
  location?: string;
}

export interface PaymentDto {
  id: number;
  paymentMethod: string;
  amount: number;
  status: string;
  transactionId?: string;
  payosPaymentId?: string;
  payosPaymentUrl?: string;
  payosQrCode?: string;
  description?: string;
  createdAt: string;
  paidAt?: string;
}

export interface CancelOrderDto {
  orderId: number;
  reason: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrderTrackingService {
  private apiUrl = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) { }

  /**
   * Lấy thông tin tracking của đơn hàng theo ID
   */
  trackOrder(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/track/${orderId}`);
  }

  /**
   * Lấy thông tin tracking của đơn hàng theo order number
   */
  trackOrderByNumber(orderNumber: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/track/number/${orderNumber}`);
  }

  /**
   * Lấy danh sách đơn hàng của user hiện tại
   */
  getMyOrders(): Observable<any> {
    return this.http.get(`${this.apiUrl}/my-orders`);
  }

  /**
   * Hủy đơn hàng
   */
  cancelOrder(cancelDto: CancelOrderDto): Observable<any> {
    return this.http.post(`${this.apiUrl}/cancel`, cancelDto);
  }

  /**
   * Yêu cầu trả hàng
   */
  requestReturn(orderId: number, reason: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${orderId}/return`, null, {
      params: { reason }
    });
  }

  /**
   * Format trạng thái đơn hàng
   */
  getStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'Chờ xử lý';
      case 'CONFIRMED': return 'Đã xác nhận';
      case 'PROCESSING': return 'Đang xử lý';
      case 'SHIPPED': return 'Đã gửi hàng';
      case 'DELIVERED': return 'Đã giao hàng';
      case 'CANCELLED': return 'Đã hủy';
      case 'RETURNED': return 'Đã trả hàng';
      default: return status;
    }
  }

  /**
   * Format trạng thái thanh toán
   */
  getPaymentStatusLabel(status: string): string {
    switch (status) {
      case 'PENDING': return 'Chờ thanh toán';
      case 'PAID': return 'Đã thanh toán';
      case 'FAILED': return 'Thanh toán thất bại';
      case 'CANCELLED': return 'Đã hủy';
      case 'REFUNDED': return 'Đã hoàn tiền';
      default: return status;
    }
  }

  /**
   * Lấy màu sắc cho trạng thái
   */
  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING': return '#ff9800';
      case 'CONFIRMED': return '#2196f3';
      case 'PROCESSING': return '#9c27b0';
      case 'SHIPPED': return '#3f51b5';
      case 'DELIVERED': return '#4caf50';
      case 'CANCELLED': return '#f44336';
      case 'RETURNED': return '#607d8b';
      default: return '#757575';
    }
  }
}
