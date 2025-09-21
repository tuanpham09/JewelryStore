import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { AuthService } from '../auth.service';
import { OrderTrackingService, OrderTrackingDto, CancelOrderDto } from '../services/order-tracking.service';
import { CancelOrderDialogComponent } from './cancel-order-dialog/cancel-order-dialog.component';
import { ReturnOrderDialogComponent } from './return-order-dialog/return-order-dialog.component';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';
import { BreadcrumbComponent, BreadcrumbItem } from '../shared/breadcrumb/breadcrumb';

interface OrderItem {
    id: string;
    productId: string;
    name: string;
    price: number;
    quantity: number;
    image: string;
    size?: string;
    color?: string;
}

interface Order {
    id: string;
    orderNumber: string;
    orderDate: string;
    status: 'pending' | 'confirmed' | 'shipping' | 'delivered' | 'cancelled';
    statusText: string;
    totalAmount: number;
    items: OrderItem[];
    shippingAddress: string;
    paymentMethod: string;
    paymentStatus: string;
    trackingNumber?: string;
    estimatedDelivery?: string;
}

@Component({
    selector: 'app-order-history',
    imports: [
        CommonModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatChipsModule,
        MatTabsModule,
        MatDividerModule,
        Header,
        Footer,
        BreadcrumbComponent
    ],
    templateUrl: 'order-history.html',
    styleUrl: 'order-history.css'
})
export class OrderHistory implements OnInit {
    currentUser: any = null;
    selectedTab: number = 0;
    orders: any[] = []; // Sử dụng any[] để phù hợp với API response
    isLoading = false;
    error: string | null = null;

    // Breadcrumb items
    breadcrumbItems: BreadcrumbItem[] = [
        { label: 'Trang chủ', url: '/home' },
        { label: 'Lịch sử đơn hàng', active: true }
    ];

    constructor(
        public router: Router,
        private authService: AuthService,
        private orderTrackingService: OrderTrackingService,
        private snackBar: MatSnackBar,
        private dialog: MatDialog
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        this.loadOrders();
    }

    loadOrders() {
        this.isLoading = true;
        this.error = null;

        console.log('Loading orders from API...');
        
        this.orderTrackingService.getMyOrders().subscribe({
            next: (response) => {
                console.log('Orders loaded:', response);
                if (response.success) {
                    this.orders = response.data || [];
                } else {
                    this.error = response.message || 'Không thể tải danh sách đơn hàng';
                }
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading orders:', error);
                this.error = 'Có lỗi xảy ra khi tải danh sách đơn hàng';
                this.isLoading = false;
                
                // Fallback to mock data for development
                this.loadMockOrders();
            }
        });
    }

    private loadMockOrders() {
        // Mock data - fallback khi API không hoạt động
        this.orders = [
            {
                orderId: 1,
                orderNumber: 'ORD-2024-001',
                status: 'DELIVERED',
                statusDescription: 'Đã giao hàng',
                total: 1540000.00,
                createdAt: '2024-01-15T10:00:00Z',
                updatedAt: '2024-01-18T15:30:00Z',
                deliveredAt: '2024-01-18T15:30:00Z',
                cancelledAt: null,
                cancellationReason: null,
                customerName: 'Nguyễn Văn A',
                customerPhone: '0123456789',
                customerEmail: 'user@example.com',
                shippingAddress: '123 Đường ABC, Quận 1, TP.HCM',
                shippingMethod: 'Giao hàng tiêu chuẩn',
                trackingNumber: 'VN123456789',
                estimatedDelivery: '2024-01-18',
                actualDelivery: '2024-01-18T15:30:00Z',
                paymentMethod: 'PAYOS',
                paymentStatus: 'PAID',
                paymentReference: 'test_payment_id',
                paidAt: '2024-01-15T10:05:00Z',
                items: [
                    {
                        id: 1,
                        productId: 1,
                        productName: 'Vòng tay ngọc trai Corrine - vàng mờ',
                        productSku: 'SKU001',
                        productImage: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=100&h=100&fit=crop&crop=center',
                        quantity: 1,
                        unitPrice: 990000.00,
                        subtotal: 990000.00,
                        discountAmount: 0.00,
                        finalPrice: 990000.00
                    },
                    {
                        id: 2,
                        productId: 2,
                        productName: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
                        productSku: 'SKU002',
                        productImage: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=100&h=100&fit=crop&crop=center',
                        quantity: 1,
                        unitPrice: 550000.00,
                        subtotal: 550000.00,
                        discountAmount: 0.00,
                        finalPrice: 550000.00
                    }
                ],
                statusHistory: [
                    {
                        status: 'PENDING',
                        statusDescription: 'Đơn hàng đã được tạo',
                        note: 'Đơn hàng đã được tạo và đang chờ xử lý',
                        timestamp: '2024-01-15T10:00:00Z',
                        updatedBy: 'System'
                    },
                    {
                        status: 'CONFIRMED',
                        statusDescription: 'Đơn hàng đã được xác nhận',
                        note: 'Đơn hàng đã được xác nhận và đang chuẩn bị',
                        timestamp: '2024-01-15T11:00:00Z',
                        updatedBy: 'Admin'
                    },
                    {
                        status: 'SHIPPED',
                        statusDescription: 'Đơn hàng đã được gửi',
                        note: 'Đơn hàng đã được gửi đi',
                        timestamp: '2024-01-16T09:00:00Z',
                        updatedBy: 'System'
                    },
                    {
                        status: 'DELIVERED',
                        statusDescription: 'Đơn hàng đã được giao',
                        note: 'Đơn hàng đã được giao thành công',
                        timestamp: '2024-01-18T15:30:00Z',
                        updatedBy: 'System'
                    }
                ]
            }
        ];
    }

    getOrdersByStatus(status: string): any[] {
        if (status === 'all') {
            return this.orders;
        }
        return this.orders.filter(order => order.status === status);
    }

    getStatusChipClass(status: string): string {
        switch (status) {
            case 'PENDING':
                return 'status-pending';
            case 'CONFIRMED':
                return 'status-confirmed';
            case 'PROCESSING':
                return 'status-processing';
            case 'SHIPPED':
                return 'status-shipping';
            case 'DELIVERED':
                return 'status-delivered';
            case 'CANCELLED':
                return 'status-cancelled';
            default:
                return 'status-pending';
        }
    }

    getStatusLabel(status: string): string {
        return this.orderTrackingService.getStatusLabel(status);
    }

    getPaymentStatusLabel(status: string): string {
        return this.orderTrackingService.getPaymentStatusLabel(status);
    }

    getStatusColor(status: string): string {
        return this.orderTrackingService.getStatusColor(status);
    }

    getStatusIcon(status: string): string {
        switch (status) {
            case 'pending':
                return 'schedule';
            case 'confirmed':
                return 'check_circle';
            case 'shipping':
                return 'local_shipping';
            case 'delivered':
                return 'done_all';
            case 'cancelled':
                return 'cancel';
            default:
                return 'schedule';
        }
    }

    goBack() {
        this.router.navigate(['/']);
    }

    navigateToProduct(productId: string) {
        this.router.navigate(['/product', productId]);
    }

    reorder(order: any) {
        // Logic đặt lại đơn hàng
        console.log('Reorder:', order);
        this.snackBar.open('Chức năng đặt lại đơn hàng đang được phát triển', 'Đóng', {
            duration: 3000
        });
    }

    trackOrder(order: any) {
        // Logic theo dõi đơn hàng
        console.log('Track order:', order);
        this.router.navigate(['/order-tracking', order.orderId]);
    }

    cancelOrder(order: any) {
        if (order.status === 'PENDING' || order.status === 'CONFIRMED') {
            const dialogRef = this.dialog.open(CancelOrderDialogComponent, {
                width: '600px',
                maxWidth: '95vw',
                maxHeight: '90vh',
                data: {
                    orderNumber: order.orderNumber,
                    orderId: order.orderId
                },
                disableClose: true,
                panelClass: 'cancel-order-dialog-container'
            });

            dialogRef.afterClosed().subscribe(result => {
                if (result && result.confirmed) {
                    const cancelDto: CancelOrderDto = {
                        orderId: order.orderId,
                        reason: result.reason
                    };

                    this.orderTrackingService.cancelOrder(cancelDto).subscribe({
                        next: (response) => {
                            if (response.success) {
                                this.snackBar.open('Đơn hàng đã được hủy thành công', 'Đóng', {
                                    duration: 3000
                                });
                                this.loadOrders(); // Reload orders
                            } else {
                                this.snackBar.open('Không thể hủy đơn hàng: ' + response.message, 'Đóng', {
                                    duration: 5000
                                });
                            }
                        },
                        error: (error) => {
                            console.error('Error cancelling order:', error);
                            this.snackBar.open('Có lỗi xảy ra khi hủy đơn hàng', 'Đóng', {
                                duration: 3000
                            });
                        }
                    });
                }
            });
        } else {
            this.snackBar.open('Không thể hủy đơn hàng ở trạng thái này', 'Đóng', {
                duration: 3000
            });
        }
    }

    requestReturn(order: any) {
        if (order.status === 'DELIVERED') {
            const dialogRef = this.dialog.open(ReturnOrderDialogComponent, {
                width: '600px',
                maxWidth: '95vw',
                maxHeight: '90vh',
                data: {
                    orderNumber: order.orderNumber,
                    orderId: order.orderId
                },
                disableClose: true,
                panelClass: 'return-order-dialog-container'
            });

            dialogRef.afterClosed().subscribe(result => {
                if (result && result.confirmed) {
                    this.orderTrackingService.requestReturn(order.orderId, result.reason).subscribe({
                        next: (response) => {
                            if (response.success) {
                                this.snackBar.open('Yêu cầu trả hàng đã được gửi thành công', 'Đóng', {
                                    duration: 3000
                                });
                                this.loadOrders(); // Reload orders
                            } else {
                                this.snackBar.open('Không thể gửi yêu cầu trả hàng: ' + response.message, 'Đóng', {
                                    duration: 5000
                                });
                            }
                        },
                        error: (error) => {
                            console.error('Error requesting return:', error);
                            this.snackBar.open('Có lỗi xảy ra khi gửi yêu cầu trả hàng', 'Đóng', {
                                duration: 3000
                            });
                        }
                    });
                }
            });
        } else {
            this.snackBar.open('Chỉ có thể trả hàng khi đơn hàng đã được giao', 'Đóng', {
                duration: 3000
            });
        }
    }

    formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    formatPrice(price: number): string {
        const correctPrice = this.getCorrectPrice(price);
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(correctPrice);
    }

    // Fix: Nếu giá quá lớn (có thể bị nhân với 1000), chia cho 1000
    getCorrectPrice(price: number): number {
        return price > 100000000 ? price / 1000 : price;
    }

    canCancelOrder(order: any): boolean {
        return order.status === 'PENDING' || order.status === 'CONFIRMED';
    }

    canReturnOrder(order: any): boolean {
        return order.status === 'DELIVERED';
    }
}
