import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../services/cart.service';
import { AuthService } from '../auth.service';
import { PaymentService } from '../services/payment.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

@Component({
    selector: 'app-checkout',
    imports: [
        CommonModule,
        ReactiveFormsModule,
        MatButtonModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatIconModule,
        MatSnackBarModule,
        MatDividerModule,
        Header,
        Footer
    ],
    templateUrl: './checkout.html',
    styleUrl: './checkout.css'
})
export class Checkout implements OnInit {
    checkoutForm: FormGroup;
    cartItems: CartItem[] = [];
    cartTotal = 0;
    cartItemCount = 0;
    isProcessing = false;

    constructor(
        private fb: FormBuilder,
        private cartService: CartService,
        private authService: AuthService,
        private paymentService: PaymentService,
        public router: Router,
        private snackBar: MatSnackBar
    ) {
        this.checkoutForm = this.fb.group({
            shippingName: ['', [Validators.required, Validators.minLength(2)]],
            shippingPhone: ['', [Validators.required, Validators.pattern(/^[0-9]{10,11}$/)]],
            shippingAddress: ['', [Validators.required, Validators.minLength(10)]],
            notes: ['']
        });
    }

    ngOnInit() {
        // Kiểm tra đăng nhập trước
        const currentUser = this.authService.getCurrentUser();
        if (!currentUser) {
            this.snackBar.open('Vui lòng đăng nhập để thanh toán', 'Đóng', {
                duration: 3000
            });
            this.router.navigate(['/login']);
            return;
        }

        // Load cart items
        this.cartService.cartItems$.subscribe(cartItems => {
            this.cartItems = cartItems;
            this.cartItemCount = this.cartService.getCartItemCount();
            this.cartTotal = this.cartService.getCartTotal();
            
            if (cartItems.length === 0) {
                this.router.navigate(['/cart']);
            }
        });
    }

    onSubmit() {
        if (this.checkoutForm.valid && this.cartItems.length > 0) {
            this.isProcessing = true;
            
            const orderData = {
                shippingName: this.checkoutForm.value.shippingName,
                shippingPhone: this.checkoutForm.value.shippingPhone,
                shippingAddress: this.checkoutForm.value.shippingAddress,
                notes: this.checkoutForm.value.notes,
                items: this.cartItems.map(item => ({
                    productId: item.productId,
                    quantity: item.quantity,
                    sizeValue: item.sizeValue,
                    colorValue: item.colorValue
                }))
            };

            this.paymentService.createOrder(orderData).subscribe({
                next: (response: any) => {
                    if (response.success) {
                        console.log('Order createdsssssssssssssssssssssssssss:', response.data);
                        this.createPayment(response.data.id);


                    } else {
                        this.snackBar.open('Có lỗi xảy ra khi tạo đơn hàng', 'Đóng', {
                            duration: 3000
                        });
                        this.isProcessing = false;
                    }
                },
                error: (error: any) => {
                    console.error('Error creating order:', error);
                    this.snackBar.open('Có lỗi xảy ra khi tạo đơn hàng', 'Đóng', {
                        duration: 3000
                    });
                    this.isProcessing = false;
                }
            });
        } else {
            this.snackBar.open('Vui lòng điền đầy đủ thông tin', 'Đóng', {
                duration: 3000
            });
        }
    }

    private createPayment(orderId: number) {
        console.log('Creating payment for order:', orderId);
        
        this.paymentService.createPaymentForOrder(orderId).subscribe({
            next: (response: any) => {
                console.log('Payment response1111111111:', response);
                
                if (response.success) {
                    console.log('====> Payment created successfully:', response.data);
                    
                    // Redirect to payment URL
                    if (response.data?.checkoutUrl) {
                        console.log('Redirecting to PayOS:', response.data.checkoutUrl);
                        // Chỉ xóa giỏ hàng khi chuyển đến trang thanh toán
                        this.cartService.clearCart().subscribe();
                        window.location.href = response.data.checkoutUrl;
                    } else {
                        console.error('xXxxxxxxx ===> No checkout URL found in response:', response);
                        this.snackBar.open('Đã tạo đơn hàng thành công nhưng không thể chuyển đến trang thanh toán', 'Đóng', {
                            duration: 5000
                        });
                        this.router.navigate(['/']);
                    }
                } else {
                    console.error('Payment creation failed:', response);
                    this.snackBar.open('Có lỗi xảy ra khi tạo thanh toán: ' + (response.message || 'Unknown error'), 'Đóng', {
                        duration: 5000
                    });
                    this.isProcessing = false;
                }
            },
            error: (error: any) => {
                console.error('Error creating payment:', error);
                this.snackBar.open('Có lỗi xảy ra khi tạo thanh toán: ' + (error.error?.message || error.message || 'Unknown error'), 'Đóng', {
                    duration: 5000
                });
                this.isProcessing = false;
            }
        });
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    }

    getErrorMessage(fieldName: string): string {
        const field = this.checkoutForm.get(fieldName);
        if (field?.hasError('required')) {
            return 'Trường này là bắt buộc';
        }
        if (field?.hasError('minlength')) {
            return 'Độ dài tối thiểu là 2 ký tự';
        }
        if (field?.hasError('pattern')) {
            return 'Số điện thoại không hợp lệ';
        }
        return '';
    }
}
