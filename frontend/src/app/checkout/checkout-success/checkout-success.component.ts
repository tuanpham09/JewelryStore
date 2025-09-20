import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-checkout-success',
  templateUrl: './checkout-success.component.html',
  styleUrls: ['./checkout-success.component.css']
})
export class CheckoutSuccessComponent implements OnInit {
  orderNumber: string | null = null;
  isLoading = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private paymentService: PaymentService
  ) { }

  ngOnInit(): void {
    // Lấy thông tin từ query parameters
    this.route.queryParams.subscribe(params => {
      this.orderNumber = params['orderNumber'] || null;
      
      if (this.orderNumber) {
        this.loadOrderDetails();
      } else {
        this.isLoading = false;
        this.error = 'Không tìm thấy thông tin đơn hàng';
      }
    });
  }

  loadOrderDetails(): void {
    if (!this.orderNumber) return;

    this.paymentService.getOrderByNumber(this.orderNumber).subscribe({
      next: (response) => {
        if (response.success) {
          // Đơn hàng đã được thanh toán thành công
          this.isLoading = false;
        } else {
          this.error = 'Không thể tải thông tin đơn hàng';
          this.isLoading = false;
        }
      },
      error: (error) => {
        console.error('Error loading order details:', error);
        this.error = 'Có lỗi xảy ra khi tải thông tin đơn hàng';
        this.isLoading = false;
      }
    });
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

  goToOrders(): void {
    this.router.navigate(['/order-history']);
  }
}
