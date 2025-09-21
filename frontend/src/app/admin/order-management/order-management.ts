import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTabsModule } from '@angular/material/tabs';
import { MatBadgeModule } from '@angular/material/badge';
import { AdminService, AdminOrderDto, OrderItemDto } from '../../services/admin.service';

interface OrderForm {
    status: string;
    cancelReason: string;
}

@Component({
    selector: 'app-order-management',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatTableModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        MatChipsModule,
        MatSnackBarModule,
        MatProgressSpinnerModule,
        MatCheckboxModule,
        MatTooltipModule,
        MatPaginatorModule,
        MatTabsModule,
        MatBadgeModule
    ],
    templateUrl: './order-management.html',
    styleUrl: './order-management.css'
})
export class OrderManagementComponent implements OnInit {
    orders: AdminOrderDto[] = [];
    filteredOrders: AdminOrderDto[] = [];
    searchTerm: string = '';
    selectedStatus: string = 'ALL';
    isDetailDialogOpen: boolean = false;
    isStatusDialogOpen: boolean = false;
    isCancelDialogOpen: boolean = false;
    selectedOrder: AdminOrderDto | null = null;
    isLoading: boolean = false;

    // Pagination
    pageIndex: number = 0;
    pageSize: number = 20;
    totalOrders: number = 0;

    // Form data
    orderForm: OrderForm = {
        status: '',
        cancelReason: ''
    };

    // Available statuses
    availableStatuses: string[] = ['PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];
    statusFilters: string[] = ['ALL', 'PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED'];

    // Table columns
    displayedColumns: string[] = ['orderNumber', 'customer', 'status', 'totalAmount', 'paymentStatus', 'createdAt', 'actions'];

    // Statistics
    orderStats = {
        total: 0,
        pending: 0,
        processing: 0,
        shipped: 0,
        delivered: 0,
        cancelled: 0
    };

    constructor(
        private adminService: AdminService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.loadOrders();
        this.calculateStats();
    }

    loadOrders() {
        this.isLoading = true;
        console.log('Loading orders from API...');
        
        const apiCall = this.selectedStatus === 'ALL' 
            ? this.adminService.getOrders(this.pageIndex, this.pageSize)
            : this.adminService.getOrdersByStatus(this.selectedStatus, this.pageIndex, this.pageSize);

        apiCall.subscribe({
            next: (orders) => {
                console.log('Orders loaded successfully:', orders);
                this.orders = orders;
                this.filteredOrders = orders;
                this.totalOrders = orders.length; // Note: This should be total count from API
                this.isLoading = false;
                this.calculateStats();
            },
            error: (error) => {
                console.error('Error loading orders:', error);
                this.snackBar.open(`Lỗi khi tải danh sách đơn hàng: ${error.status} ${error.statusText}`, 'Đóng', { duration: 5000 });
                this.isLoading = false;
            }
        });
    }

    filterOrders() {
        this.filteredOrders = this.orders.filter(order => {
            const matchesSearch = order.orderNumber.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                order.customerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                order.customerEmail.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                order.customerPhone.includes(this.searchTerm);
            return matchesSearch;
        });
    }

    onStatusFilterChange() {
        this.pageIndex = 0;
        this.loadOrders();
    }

    openDetailDialog(order: AdminOrderDto) {
        this.selectedOrder = order;
        this.isDetailDialogOpen = true;
    }

    openStatusDialog(order: AdminOrderDto) {
        this.selectedOrder = order;
        this.orderForm.status = order.status;
        this.isStatusDialogOpen = true;
    }

    openCancelDialog(order: AdminOrderDto) {
        this.selectedOrder = order;
        this.orderForm.cancelReason = '';
        this.isCancelDialogOpen = true;
    }

    closeDialogs() {
        this.isDetailDialogOpen = false;
        this.isStatusDialogOpen = false;
        this.isCancelDialogOpen = false;
        this.selectedOrder = null;
        this.resetForm();
    }

    resetForm() {
        this.orderForm = {
            status: '',
            cancelReason: ''
        };
    }

    updateOrderStatus() {
        if (this.selectedOrder && this.orderForm.status) {
            this.isLoading = true;
            this.adminService.updateOrderStatus(this.selectedOrder.id, this.orderForm.status).subscribe({
                next: (updatedOrder) => {
                    const index = this.orders.findIndex(o => o.id === updatedOrder.id);
                    if (index !== -1) {
                        this.orders[index] = updatedOrder;
                    }
                    this.snackBar.open('Cập nhật trạng thái đơn hàng thành công!', 'Đóng', { duration: 3000 });
                    this.filterOrders();
                    this.calculateStats();
                    this.closeDialogs();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error updating order status:', error);
                    this.snackBar.open('Lỗi khi cập nhật trạng thái đơn hàng', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    cancelOrder() {
        if (this.selectedOrder && this.orderForm.cancelReason.trim()) {
            this.isLoading = true;
            this.adminService.cancelOrder(this.selectedOrder.id, this.orderForm.cancelReason).subscribe({
                next: (cancelledOrder) => {
                    const index = this.orders.findIndex(o => o.id === cancelledOrder.id);
                    if (index !== -1) {
                        this.orders[index] = cancelledOrder;
                    }
                    this.snackBar.open('Hủy đơn hàng thành công!', 'Đóng', { duration: 3000 });
                    this.filterOrders();
                    this.calculateStats();
                    this.closeDialogs();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error cancelling order:', error);
                    this.snackBar.open('Lỗi khi hủy đơn hàng', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    onPageChange(event: PageEvent) {
        this.pageIndex = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadOrders();
    }

    calculateStats() {
        this.orderStats = {
            total: this.orders.length,
            pending: this.orders.filter(o => o.status === 'PENDING').length,
            processing: this.orders.filter(o => o.status === 'PROCESSING').length,
            shipped: this.orders.filter(o => o.status === 'SHIPPED').length,
            delivered: this.orders.filter(o => o.status === 'DELIVERED').length,
            cancelled: this.orders.filter(o => o.status === 'CANCELLED').length
        };
    }

    formatDate(dateString: string): string {
        return new Date(dateString).toLocaleDateString('vi-VN');
    }

    formatCurrency(amount: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    getStatusDisplayName(status: string): string {
        const statusMap: { [key: string]: string } = {
            'PENDING': 'Chờ xử lý',
            'PROCESSING': 'Đang xử lý',
            'SHIPPED': 'Đã giao hàng',
            'DELIVERED': 'Đã nhận hàng',
            'CANCELLED': 'Đã hủy'
        };
        return statusMap[status] || status;
    }

    getStatusColor(status: string): string {
        const colorMap: { [key: string]: string } = {
            'PENDING': '#ffc107',
            'PROCESSING': '#17a2b8',
            'SHIPPED': '#28a745',
            'DELIVERED': '#6f42c1',
            'CANCELLED': '#dc3545'
        };
        return colorMap[status] || '#6c757d';
    }

    getPaymentStatusColor(status: string): string {
        const colorMap: { [key: string]: string } = {
            'PENDING': '#ffc107',
            'PAID': '#28a745',
            'FAILED': '#dc3545',
            'REFUNDED': '#6c757d'
        };
        return colorMap[status] || '#6c757d';
    }

    getPaymentStatusDisplayName(status: string): string {
        const statusMap: { [key: string]: string } = {
            'PENDING': 'Chờ thanh toán',
            'PAID': 'Đã thanh toán',
            'FAILED': 'Thanh toán thất bại',
            'REFUNDED': 'Đã hoàn tiền'
        };
        return statusMap[status] || status;
    }

    canUpdateStatus(order: AdminOrderDto): boolean {
        return order.status !== 'DELIVERED' && order.status !== 'CANCELLED';
    }

    canCancelOrder(order: AdminOrderDto): boolean {
        return order.status === 'PENDING' || order.status === 'PROCESSING';
    }
}
