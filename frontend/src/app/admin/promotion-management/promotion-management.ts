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
import { AdminService, PromotionDto } from '../../services/admin.service';

interface PromotionForm {
    name: string;
    description: string;
    type: string;
    value: number;
    minOrderAmount: number;
    code: string;
    startDate: string;
    endDate: string;
    active: boolean;
    usageLimit: number;
    applicableProducts: string;
    applicableCategories: string;
    applicableProductIds: string;
}

@Component({
    selector: 'app-promotion-management',
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
        MatNativeDateModule
    ],
    templateUrl: './promotion-management.html',
    styleUrl: './promotion-management.css'
})
export class PromotionManagementComponent implements OnInit {
    promotions: PromotionDto[] = [];
    filteredPromotions: PromotionDto[] = [];
    searchTerm: string = '';
    selectedStatus: string = 'ALL';
    isAddDialogOpen: boolean = false;
    isEditDialogOpen: boolean = false;
    isDetailDialogOpen: boolean = false;
    editingPromotion: PromotionDto | null = null;
    selectedPromotion: PromotionDto | null = null;
    isLoading: boolean = false;

    // Form data
    promotionForm: PromotionForm = {
        name: '',
        description: '',
        type: 'PERCENTAGE',
        value: 0,
        minOrderAmount: 0,
        code: '',
        startDate: '',
        endDate: '',
        active: true,
        usageLimit: 0,
        applicableProducts: 'ALL',
        applicableCategories: '',
        applicableProductIds: ''
    };

    // Available options
    promotionTypes: string[] = ['PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING'];
    applicableProductsOptions: string[] = ['ALL', 'CATEGORIES', 'PRODUCTS'];
    statusFilters: string[] = ['ALL', 'ACTIVE', 'INACTIVE', 'EXPIRED', 'UPCOMING'];

    // Table columns
    displayedColumns: string[] = ['name', 'type', 'value', 'code', 'status', 'usage', 'dates', 'actions'];

    // Statistics
    promotionStats = {
        total: 0,
        active: 0,
        inactive: 0,
        expired: 0,
        upcoming: 0
    };

    constructor(
        private adminService: AdminService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.loadPromotions();
        this.calculateStats();
    }

    loadPromotions() {
        this.isLoading = true;
        console.log('Loading promotions from API...');
        this.adminService.getPromotions().subscribe({
            next: (promotions) => {
                console.log('Promotions loaded successfully:', promotions);
                this.promotions = promotions;
                this.filteredPromotions = promotions;
                this.isLoading = false;
                this.calculateStats();
            },
            error: (error) => {
                console.error('Error loading promotions:', error);
                this.snackBar.open(`Lỗi khi tải danh sách khuyến mãi: ${error.status} ${error.statusText}`, 'Đóng', { duration: 5000 });
                this.isLoading = false;
            }
        });
    }

    filterPromotions() {
        this.filteredPromotions = this.promotions.filter(promotion => {
            const matchesSearch = promotion.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                promotion.description.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                promotion.code.toLowerCase().includes(this.searchTerm.toLowerCase());
            
            const matchesStatus = this.selectedStatus === 'ALL' || this.getPromotionStatus(promotion) === this.selectedStatus;
            
            return matchesSearch && matchesStatus;
        });
    }

    onStatusFilterChange() {
        this.filterPromotions();
    }

    openAddDialog() {
        this.resetForm();
        this.isAddDialogOpen = true;
    }

    openEditDialog(promotion: PromotionDto) {
        this.editingPromotion = promotion;
        this.promotionForm = {
            name: promotion.name,
            description: promotion.description,
            type: promotion.type,
            value: promotion.value,
            minOrderAmount: promotion.minOrderAmount,
            code: promotion.code,
            startDate: promotion.startDate,
            endDate: promotion.endDate,
            active: promotion.active,
            usageLimit: promotion.usageLimit,
            applicableProducts: promotion.applicableProducts,
            applicableCategories: promotion.applicableCategories || '',
            applicableProductIds: promotion.applicableProductIds || ''
        };
        this.isEditDialogOpen = true;
    }

    openDetailDialog(promotion: PromotionDto) {
        this.selectedPromotion = promotion;
        this.isDetailDialogOpen = true;
    }

    closeDialogs() {
        this.isAddDialogOpen = false;
        this.isEditDialogOpen = false;
        this.isDetailDialogOpen = false;
        this.editingPromotion = null;
        this.selectedPromotion = null;
        this.resetForm();
    }

    resetForm() {
        this.promotionForm = {
            name: '',
            description: '',
            type: 'PERCENTAGE',
            value: 0,
            minOrderAmount: 0,
            code: '',
            startDate: '',
            endDate: '',
            active: true,
            usageLimit: 0,
            applicableProducts: 'ALL',
            applicableCategories: '',
            applicableProductIds: ''
        };
    }

    savePromotion() {
        if (this.validateForm()) {
            this.isLoading = true;

            const promotionData: PromotionDto = {
                id: this.editingPromotion?.id || 0,
                name: this.promotionForm.name,
                description: this.promotionForm.description,
                type: this.promotionForm.type,
                value: this.promotionForm.value,
                minOrderAmount: this.promotionForm.minOrderAmount,
                code: this.promotionForm.code,
                startDate: this.promotionForm.startDate,
                endDate: this.promotionForm.endDate,
                active: this.promotionForm.active,
                usageLimit: this.promotionForm.usageLimit,
                usedCount: this.editingPromotion?.usedCount || 0,
                applicableProducts: this.promotionForm.applicableProducts,
                applicableCategories: this.promotionForm.applicableCategories,
                applicableProductIds: this.promotionForm.applicableProductIds,
                createdAt: this.editingPromotion?.createdAt || new Date().toISOString(),
                updatedAt: new Date().toISOString()
            };

            const apiCall = this.isEditDialogOpen 
                ? this.adminService.updatePromotion(this.editingPromotion!.id, promotionData)
                : this.adminService.createPromotion(promotionData);

            apiCall.subscribe({
                next: (savedPromotion) => {
                    if (this.isEditDialogOpen) {
                        const index = this.promotions.findIndex(p => p.id === savedPromotion.id);
                        if (index !== -1) {
                            this.promotions[index] = savedPromotion;
                        }
                    } else {
                        this.promotions.unshift(savedPromotion);
                    }
                    this.snackBar.open(`${this.isEditDialogOpen ? 'Cập nhật' : 'Tạo'} khuyến mãi thành công!`, 'Đóng', { duration: 3000 });
                    this.filterPromotions();
                    this.calculateStats();
                    this.closeDialogs();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error saving promotion:', error);
                    this.snackBar.open(`Lỗi khi ${this.isEditDialogOpen ? 'cập nhật' : 'tạo'} khuyến mãi`, 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    togglePromotionStatus(promotion: PromotionDto) {
        this.isLoading = true;
        this.adminService.togglePromotionStatus(promotion.id).subscribe({
            next: (updatedPromotion) => {
                const index = this.promotions.findIndex(p => p.id === updatedPromotion.id);
                if (index !== -1) {
                    this.promotions[index] = updatedPromotion;
                }
                this.snackBar.open(`${updatedPromotion.active ? 'Kích hoạt' : 'Vô hiệu hóa'} khuyến mãi thành công!`, 'Đóng', { duration: 3000 });
                this.filterPromotions();
                this.calculateStats();
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error toggling promotion status:', error);
                this.snackBar.open('Lỗi khi thay đổi trạng thái khuyến mãi', 'Đóng', { duration: 3000 });
                this.isLoading = false;
            }
        });
    }

    deletePromotion(promotion: PromotionDto) {
        if (confirm(`Bạn có chắc chắn muốn xóa khuyến mãi "${promotion.name}"?`)) {
            this.isLoading = true;
            this.adminService.deletePromotion(promotion.id).subscribe({
                next: () => {
                    this.promotions = this.promotions.filter(p => p.id !== promotion.id);
                    this.snackBar.open('Xóa khuyến mãi thành công!', 'Đóng', { duration: 3000 });
                    this.filterPromotions();
                    this.calculateStats();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error deleting promotion:', error);
                    this.snackBar.open('Lỗi khi xóa khuyến mãi', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    calculateStats() {
        this.promotionStats = {
            total: this.promotions.length,
            active: this.promotions.filter(p => p.active && this.isPromotionActive(p)).length,
            inactive: this.promotions.filter(p => !p.active).length,
            expired: this.promotions.filter(p => this.isPromotionExpired(p)).length,
            upcoming: this.promotions.filter(p => this.isPromotionUpcoming(p)).length
        };
    }

    isPromotionActive(promotion: PromotionDto): boolean {
        const now = new Date();
        const startDate = new Date(promotion.startDate);
        const endDate = new Date(promotion.endDate);
        return promotion.active && now >= startDate && now <= endDate;
    }

    isPromotionExpired(promotion: PromotionDto): boolean {
        const now = new Date();
        const endDate = new Date(promotion.endDate);
        return now > endDate;
    }

    isPromotionUpcoming(promotion: PromotionDto): boolean {
        const now = new Date();
        const startDate = new Date(promotion.startDate);
        return now < startDate;
    }

    getPromotionStatus(promotion: PromotionDto): string {
        if (this.isPromotionExpired(promotion)) return 'EXPIRED';
        if (this.isPromotionUpcoming(promotion)) return 'UPCOMING';
        if (promotion.active) return 'ACTIVE';
        return 'INACTIVE';
    }

    validateForm(): boolean {
        if (!this.promotionForm.name.trim()) {
            this.snackBar.open('Vui lòng nhập tên khuyến mãi!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.promotionForm.code.trim()) {
            this.snackBar.open('Vui lòng nhập mã khuyến mãi!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.promotionForm.value <= 0) {
            this.snackBar.open('Vui lòng nhập giá trị khuyến mãi hợp lệ!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.promotionForm.startDate) {
            this.snackBar.open('Vui lòng chọn ngày bắt đầu!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.promotionForm.endDate) {
            this.snackBar.open('Vui lòng chọn ngày kết thúc!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (new Date(this.promotionForm.startDate) >= new Date(this.promotionForm.endDate)) {
            this.snackBar.open('Ngày bắt đầu phải trước ngày kết thúc!', 'Đóng', { duration: 3000 });
            return false;
        }
        return true;
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

    getTypeDisplayName(type: string): string {
        const typeMap: { [key: string]: string } = {
            'PERCENTAGE': 'Phần trăm',
            'FIXED_AMOUNT': 'Số tiền cố định',
            'FREE_SHIPPING': 'Miễn phí vận chuyển'
        };
        return typeMap[type] || type;
    }

    getStatusDisplayName(status: string): string {
        const statusMap: { [key: string]: string } = {
            'ACTIVE': 'Hoạt động',
            'INACTIVE': 'Không hoạt động',
            'EXPIRED': 'Hết hạn',
            'UPCOMING': 'Sắp diễn ra'
        };
        return statusMap[status] || status;
    }

    getStatusColor(status: string): string {
        const colorMap: { [key: string]: string } = {
            'ACTIVE': '#28a745',
            'INACTIVE': '#6c757d',
            'EXPIRED': '#dc3545',
            'UPCOMING': '#ffc107'
        };
        return colorMap[status] || '#6c757d';
    }

    generatePromotionCode(): string {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
        let result = '';
        for (let i = 0; i < 8; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        return result;
    }

    onGenerateCode() {
        this.promotionForm.code = this.generatePromotionCode();
    }
}
