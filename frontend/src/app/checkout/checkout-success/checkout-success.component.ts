import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../services/payment.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-checkout-success',
  templateUrl: './checkout-success.component.html',
  styleUrls: ['./checkout-success.component.css']
})
export class CheckoutSuccessComponent implements OnInit {
  orderNumber: string | null = null;
  orderCode: string | null = null;
  paymentId: string | null = null;
  status: string | null = null;
  isLoading = true;
  error: string | null = null;
  orderDetails: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private paymentService: PaymentService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // Lấy thông tin từ query parameters
    this.route.queryParams.subscribe(params => {
      this.orderCode = params['orderCode'] || null;
      this.paymentId = params['id'] || null;
      this.status = params['status'] || null;
      const code = params['code'] || null;
      const cancel = params['cancel'] || null;
      
      console.log('Payment callback params:', params);
      
      // Xử lý theo trạng thái từ PAYOS
      if (this.status === 'PAID' && code === '00' && cancel === 'false') {
        this.handlePaymentSuccess();
      } else if (this.status === 'CANCELLED' || cancel === 'true') {
        this.handlePaymentCancelled();
      } else if (this.status === 'FAILED' || code !== '00') {
        this.handlePaymentFailed();
      } else {
        this.isLoading = false;
        this.error = 'Thông tin thanh toán không hợp lệ';
      }
    });
  }

  handlePaymentSuccess(): void {
    console.log('Handling payment success for orderCode:', this.orderCode);
    
    // Call API để cập nhật trạng thái đơn hàng
    this.paymentService.confirmPayment(this.orderCode!, this.paymentId!).subscribe({
      next: (response) => {
        console.log('Payment confirmation response:', response);
        if (response.success) {
          this.orderDetails = response.data;
          this.orderNumber = this.orderDetails?.orderNumber;
          this.isLoading = false;
          this.snackBar.open('Thanh toán thành công!', 'Đóng', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
        } else {
          this.error = 'Không thể xác nhận thanh toán';
          this.isLoading = false;
        }
      },
      error: (error) => {
        console.error('Error confirming payment:', error);
        this.error = 'Có lỗi xảy ra khi xác nhận thanh toán';
        this.isLoading = false;
      }
    });
  }

  handlePaymentCancelled(): void {
    this.isLoading = false;
    this.error = 'Thanh toán đã bị hủy';
    this.snackBar.open('Thanh toán đã bị hủy', 'Đóng', {
      duration: 5000
    });
  }

  handlePaymentFailed(): void {
    this.isLoading = false;
    this.error = 'Thanh toán thất bại';
    this.snackBar.open('Thanh toán thất bại. Vui lòng thử lại', 'Đóng', {
      duration: 5000
    });
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

  goToOrders(): void {
    this.router.navigate(['/order-history']);
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(price);
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
