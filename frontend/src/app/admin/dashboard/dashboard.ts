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
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { AdminService, DashboardStatsDto, TopProductDto, TopCustomerDto, RevenueChartDto, OrderStatusDto } from '../../services/admin.service';

@Component({
    selector: 'app-dashboard',
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
        MatBadgeModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatGridListModule,
        MatListModule,
        MatDividerModule
    ],
    templateUrl: './dashboard.html',
    styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
    dashboardStats: DashboardStatsDto | null = null;
    isLoading: boolean = false;
    selectedTab: number = 0;

    // Chart data
    revenueChartData: any[] = [];
    orderStatusData: any[] = [];

    // Table data
    topProducts: TopProductDto[] = [];
    topCustomers: TopCustomerDto[] = [];

    // Table columns
    productColumns: string[] = ['product', 'sold', 'revenue'];
    customerColumns: string[] = ['customer', 'orders', 'spent'];

    constructor(
        private adminService: AdminService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.loadDashboardStats();
    }

    loadDashboardStats() {
        this.isLoading = true;
        console.log('Loading dashboard stats from API...');
        this.adminService.getDashboardStats().subscribe({
            next: (stats) => {
                console.log('Dashboard stats loaded successfully:', stats);
                this.dashboardStats = stats;
                this.topProducts = stats.topSellingProducts || [];
                this.topCustomers = stats.topCustomers || [];
                this.prepareChartData(stats);
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading dashboard stats:', error);
                this.isLoading = false;
            }
        });
    }

    prepareChartData(stats: DashboardStatsDto) {
        // Prepare revenue chart data
        this.revenueChartData = stats.revenueChart?.map(item => ({
            name: this.formatDate(item.date),
            value: item.revenue,
            orders: item.orders
        })) || [];

        // Prepare order status data
        this.orderStatusData = stats.orderStatusStats?.map(item => ({
            name: this.getStatusDisplayName(item.status),
            value: item.count,
            revenue: item.totalValue
        })) || [];
    }

    formatCurrency(amount: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(amount);
    }

    formatDate(dateString: string): string {
        return new Date(dateString).toLocaleDateString('vi-VN');
    }

    formatNumber(num: number): string {
        return new Intl.NumberFormat('vi-VN').format(num);
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

    getPercentageChange(current: number, previous: number): number {
        if (previous === 0) return 0;
        return ((current - previous) / previous) * 100;
    }

    getChangeColor(change: number): string {
        if (change > 0) return '#28a745';
        if (change < 0) return '#dc3545';
        return '#6c757d';
    }

    getChangeIcon(change: number): string {
        if (change > 0) return 'trending_up';
        if (change < 0) return 'trending_down';
        return 'trending_flat';
    }

    refreshStats() {
        this.loadDashboardStats();
    }

    onTabChange(index: number) {
        this.selectedTab = index;
    }

    exportPDF() {
        if (!this.dashboardStats) {
            this.snackBar.open('Không có dữ liệu để export', 'Đóng', { duration: 3000 });
            return;
        }

        this.isLoading = true;
        
        // Tạo nội dung PDF
        const content = this.generatePDFContent();
        
        // Sử dụng jsPDF để tạo PDF
        // @ts-ignore - Dynamic import for jspdf
        import('jspdf').then((jsPDF) => {
            const doc = new jsPDF.default();
            
            // Header
            doc.setFontSize(20);
            doc.setTextColor(214, 51, 132);
            doc.text('Dashboard Thống Kê', 20, 30);
            
            doc.setFontSize(12);
            doc.setTextColor(100, 100, 100);
            doc.text(`Ngày tạo: ${new Date().toLocaleDateString('vi-VN')}`, 20, 40);
            
            // Metrics
            let yPosition = 60;
            doc.setFontSize(16);
            doc.setTextColor(0, 0, 0);
            doc.text('Thống kê chính:', 20, yPosition);
            yPosition += 20;
            
            // Revenue stats
            doc.setFontSize(12);
            doc.text(`Tổng doanh thu: ${this.formatCurrency(this.dashboardStats?.totalRevenue || 0)}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Doanh thu hôm nay: ${this.formatCurrency(this.dashboardStats?.todayRevenue || 0)}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Doanh thu tháng này: ${this.formatCurrency(this.dashboardStats?.monthlyRevenue || 0)}`, 20, yPosition);
            yPosition += 25;
            
            // Orders stats
            doc.setFontSize(16);
            doc.text('Thống kê đơn hàng:', 20, yPosition);
            yPosition += 20;
            
            doc.setFontSize(12);
            doc.text(`Tổng đơn hàng: ${this.dashboardStats?.totalOrders || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Đơn hàng hôm nay: ${this.dashboardStats?.todayOrders || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Đơn hàng chờ xử lý: ${this.dashboardStats?.pendingOrders || 0}`, 20, yPosition);
            yPosition += 25;
            
            // Customer stats
            doc.setFontSize(16);
            doc.text('Thống kê khách hàng:', 20, yPosition);
            yPosition += 20;
            
            doc.setFontSize(12);
            doc.text(`Tổng khách hàng: ${this.dashboardStats?.totalCustomers || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Khách hàng mới hôm nay: ${this.dashboardStats?.newCustomersToday || 0}`, 20, yPosition);
            yPosition += 25;
            
            // Product stats
            doc.setFontSize(16);
            doc.text('Thống kê sản phẩm:', 20, yPosition);
            yPosition += 20;
            
            doc.setFontSize(12);
            doc.text(`Tổng sản phẩm: ${this.dashboardStats?.totalProducts || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Sản phẩm sắp hết hàng: ${this.dashboardStats?.lowStockProducts || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Sản phẩm hết hàng: ${this.dashboardStats?.outOfStockProducts || 0}`, 20, yPosition);
            yPosition += 15;
            doc.text(`Khuyến mãi đang hoạt động: ${this.dashboardStats?.activePromotions || 0}`, 20, yPosition);
            
            // Save PDF
            const fileName = `dashboard-report-${new Date().toISOString().split('T')[0]}.pdf`;
            doc.save(fileName);
            
            this.isLoading = false;
            this.snackBar.open('Export PDF thành công!', 'Đóng', { duration: 3000 });
        }).catch((error) => {
            console.error('Error generating PDF:', error);
            this.isLoading = false;
            this.snackBar.open('Lỗi khi export PDF', 'Đóng', { duration: 3000 });
        });
    }

    exportExcel() {
        if (!this.dashboardStats) {
            this.snackBar.open('Không có dữ liệu để export', 'Đóng', { duration: 3000 });
            return;
        }

        this.isLoading = true;
        
        // Tạo dữ liệu Excel
        const data = this.generateExcelData();
        
        // Sử dụng xlsx để tạo Excel
        // @ts-ignore - Dynamic import for xlsx
        import('xlsx').then((XLSX) => {
            const ws = XLSX.utils.json_to_sheet(data);
            const wb = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(wb, ws, 'Dashboard Report');
            
            // Save Excel
            const fileName = `dashboard-report-${new Date().toISOString().split('T')[0]}.xlsx`;
            XLSX.writeFile(wb, fileName);
            
            this.isLoading = false;
            this.snackBar.open('Export Excel thành công!', 'Đóng', { duration: 3000 });
        }).catch((error) => {
            console.error('Error generating Excel:', error);
            this.isLoading = false;
            this.snackBar.open('Lỗi khi export Excel', 'Đóng', { duration: 3000 });
        });
    }

    private generatePDFContent(): string {
        if (!this.dashboardStats) return '';
        
        return `
            Dashboard Thống Kê - ${new Date().toLocaleDateString('vi-VN')}
            
            THỐNG KÊ CHÍNH:
            - Tổng doanh thu: ${this.formatCurrency(this.dashboardStats?.totalRevenue || 0)}
            - Doanh thu hôm nay: ${this.formatCurrency(this.dashboardStats?.todayRevenue || 0)}
            - Doanh thu tháng này: ${this.formatCurrency(this.dashboardStats?.monthlyRevenue || 0)}
            
            THỐNG KÊ ĐƠN HÀNG:
            - Tổng đơn hàng: ${this.dashboardStats?.totalOrders || 0}
            - Đơn hàng hôm nay: ${this.dashboardStats?.todayOrders || 0}
            - Đơn hàng chờ xử lý: ${this.dashboardStats?.pendingOrders || 0}
            
            THỐNG KÊ KHÁCH HÀNG:
            - Tổng khách hàng: ${this.dashboardStats?.totalCustomers || 0}
            - Khách hàng mới hôm nay: ${this.dashboardStats?.newCustomersToday || 0}
            
            THỐNG KÊ SẢN PHẨM:
            - Tổng sản phẩm: ${this.dashboardStats?.totalProducts || 0}
            - Sản phẩm sắp hết hàng: ${this.dashboardStats?.lowStockProducts || 0}
            - Sản phẩm hết hàng: ${this.dashboardStats?.outOfStockProducts || 0}
            - Khuyến mãi đang hoạt động: ${this.dashboardStats?.activePromotions || 0}
        `;
    }

    private generateExcelData(): any[] {
        if (!this.dashboardStats) return [];
        
        return [
            { 'Loại thống kê': 'Tổng doanh thu', 'Giá trị': this.dashboardStats?.totalRevenue || 0, 'Đơn vị': 'VND' },
            { 'Loại thống kê': 'Doanh thu hôm nay', 'Giá trị': this.dashboardStats?.todayRevenue || 0, 'Đơn vị': 'VND' },
            { 'Loại thống kê': 'Doanh thu tháng này', 'Giá trị': this.dashboardStats?.monthlyRevenue || 0, 'Đơn vị': 'VND' },
            { 'Loại thống kê': 'Tổng đơn hàng', 'Giá trị': this.dashboardStats?.totalOrders || 0, 'Đơn vị': 'đơn' },
            { 'Loại thống kê': 'Đơn hàng hôm nay', 'Giá trị': this.dashboardStats?.todayOrders || 0, 'Đơn vị': 'đơn' },
            { 'Loại thống kê': 'Đơn hàng chờ xử lý', 'Giá trị': this.dashboardStats?.pendingOrders || 0, 'Đơn vị': 'đơn' },
            { 'Loại thống kê': 'Tổng khách hàng', 'Giá trị': this.dashboardStats?.totalCustomers || 0, 'Đơn vị': 'người' },
            { 'Loại thống kê': 'Khách hàng mới hôm nay', 'Giá trị': this.dashboardStats?.newCustomersToday || 0, 'Đơn vị': 'người' },
            { 'Loại thống kê': 'Tổng sản phẩm', 'Giá trị': this.dashboardStats?.totalProducts || 0, 'Đơn vị': 'sản phẩm' },
            { 'Loại thống kê': 'Sản phẩm sắp hết hàng', 'Giá trị': this.dashboardStats?.lowStockProducts || 0, 'Đơn vị': 'sản phẩm' },
            { 'Loại thống kê': 'Sản phẩm hết hàng', 'Giá trị': this.dashboardStats?.outOfStockProducts || 0, 'Đơn vị': 'sản phẩm' },
            { 'Loại thống kê': 'Khuyến mãi đang hoạt động', 'Giá trị': this.dashboardStats?.activePromotions || 0, 'Đơn vị': 'khuyến mãi' }
        ];
    }
}
