import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTabsModule } from '@angular/material/tabs';
import { MatDividerModule } from '@angular/material/divider';
import { AuthService } from '../auth.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

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
        Footer
    ],
    templateUrl: './order-history.html',
    styleUrl: './order-history.css'
})
export class OrderHistory implements OnInit {
    currentUser: any = null;
    selectedTab: number = 0;
    orders: Order[] = [];

    constructor(
        public router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        this.loadOrders();
    }

    loadOrders() {
        // Mock data - trong thực tế sẽ gọi API
        this.orders = [
            {
                id: '1',
                orderNumber: 'ORD-2024-001',
                orderDate: '2024-01-15',
                status: 'delivered',
                statusText: 'Đã giao hàng',
                totalAmount: 1540000,
                items: [
                    {
                        id: '1',
                        productId: '1',
                        name: 'Vòng tay ngọc trai Corrine - vàng mờ',
                        price: 990000,
                        quantity: 1,
                        image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=100&h=100&fit=crop&crop=center',
                        size: 'M',
                        color: 'Vàng mờ'
                    },
                    {
                        id: '2',
                        productId: '2',
                        name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
                        price: 550000,
                        quantity: 1,
                        image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=100&h=100&fit=crop&crop=center',
                        size: 'S',
                        color: 'Vàng hồng'
                    }
                ],
                shippingAddress: '123 Đường ABC, Quận 1, TP.HCM',
                paymentMethod: 'Thanh toán khi nhận hàng',
                trackingNumber: 'VN123456789',
                estimatedDelivery: '2024-01-18'
            },
            {
                id: '2',
                orderNumber: 'ORD-2024-002',
                orderDate: '2024-01-20',
                status: 'shipping',
                statusText: 'Đang giao hàng',
                totalAmount: 1050000,
                items: [
                    {
                        id: '3',
                        productId: '4',
                        name: 'Bông tai hoa pha lê Felicity - vàng hồng',
                        price: 1050000,
                        quantity: 1,
                        image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=100&h=100&fit=crop&crop=center',
                        size: 'R',
                        color: 'Vàng hồng'
                    }
                ],
                shippingAddress: '456 Đường XYZ, Quận 3, TP.HCM',
                paymentMethod: 'Chuyển khoản ngân hàng',
                trackingNumber: 'VN987654321',
                estimatedDelivery: '2024-01-25'
            },
            {
                id: '3',
                orderNumber: 'ORD-2024-003',
                orderDate: '2024-01-22',
                status: 'confirmed',
                statusText: 'Đã xác nhận',
                totalAmount: 750000,
                items: [
                    {
                        id: '4',
                        productId: '9',
                        name: 'Vòng tay charm hoa hồng - vàng hồng',
                        price: 750000,
                        quantity: 1,
                        image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=100&h=100&fit=crop&crop=center',
                        size: 'M',
                        color: 'Vàng hồng'
                    }
                ],
                shippingAddress: '789 Đường DEF, Quận 7, TP.HCM',
                paymentMethod: 'Ví điện tử',
                estimatedDelivery: '2024-01-28'
            }
        ];
    }

    getOrdersByStatus(status: string): Order[] {
        if (status === 'all') {
            return this.orders;
        }
        return this.orders.filter(order => order.status === status);
    }

    getStatusChipClass(status: string): string {
        switch (status) {
            case 'pending':
                return 'status-pending';
            case 'confirmed':
                return 'status-confirmed';
            case 'shipping':
                return 'status-shipping';
            case 'delivered':
                return 'status-delivered';
            case 'cancelled':
                return 'status-cancelled';
            default:
                return 'status-pending';
        }
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

    reorder(order: Order) {
        // Logic đặt lại đơn hàng
        console.log('Reorder:', order);
    }

    trackOrder(order: Order) {
        // Logic theo dõi đơn hàng
        console.log('Track order:', order);
    }

    cancelOrder(order: Order) {
        // Logic hủy đơn hàng
        console.log('Cancel order:', order);
    }
}
