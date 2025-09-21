import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-checkout-cancel',
  templateUrl: './checkout-cancel.component.html',
  styleUrls: ['./checkout-cancel.component.css']
})
export class CheckoutCancelComponent implements OnInit {
  orderNumber: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Lấy thông tin từ query parameters
    this.route.queryParams.subscribe(params => {
      this.orderNumber = params['orderNumber'] || null;
    });
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

  goToCart(): void {
    this.router.navigate(['/cart']);
  }

  tryAgain(): void {
    // Có thể redirect về trang thanh toán hoặc cart
    this.router.navigate(['/cart']);
  }
}
