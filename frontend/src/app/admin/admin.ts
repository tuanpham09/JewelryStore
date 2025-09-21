import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';
import { ProductManagementComponent } from './product-management/product-management';
import { CategoryManagementComponent } from './category-management/category-management';
import { UserManagementComponent } from './user-management/user-management';
import { OrderManagementComponent } from './order-management/order-management';
import { PromotionManagementComponent } from './promotion-management/promotion-management';
import { DashboardComponent } from './dashboard/dashboard';
import { AuthService } from '../auth.service';

interface Product {
    id: string;
    name: string;
    description: string;
    price: number;
    originalPrice?: number;
    category: string;
    material: string;
    color: string;
    size: string;
    images: string[];
    inStock: boolean;
    rating: number;
    reviewCount: number;
    tags: string[];
    createdAt: Date;
    updatedAt: Date;
}

interface ProductForm {
    name: string;
    description: string;
    price: number;
    originalPrice?: number;
    category: string;
    material: string;
    color: string;
    size: string;
    images: File[];
    inStock: boolean;
    tags: string[];
}

@Component({
    selector: 'app-admin',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,
        MatToolbarModule,
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
        Header,
        Footer,
        ProductManagementComponent,
        CategoryManagementComponent,
        UserManagementComponent,
        OrderManagementComponent,
        PromotionManagementComponent,
        DashboardComponent
    ],
    templateUrl: './admin.html',
    styleUrl: './admin.css'
})
export class AdminComponent implements OnInit {
    activeSection: string = 'dashboard';
    isCheckingPermission: boolean = true;
    hasAdminPermission: boolean = false;

    // Legacy product management properties (for future use)
    products: Product[] = [];
    filteredProducts: Product[] = [];
    searchTerm: string = '';
    selectedCategory: string = '';
    isAddDialogOpen: boolean = false;
    isEditDialogOpen: boolean = false;
    editingProduct: Product | null = null;
    isLoading: boolean = false;

    // Form data
    productForm: ProductForm = {
        name: '',
        description: '',
        price: 0,
        originalPrice: 0,
        category: '',
        material: '',
        color: '',
        size: '',
        images: [],
        inStock: true,
        tags: []
    };

    newTag: string = '';
    previewImages: string[] = [];

    // Categories and options
    categories = [
        { value: 'rings', label: 'Nhẫn' },
        { value: 'necklaces', label: 'Dây chuyền' },
        { value: 'bracelets', label: 'Vòng tay' },
        { value: 'earrings', label: 'Bông tai' },
        { value: 'watches', label: 'Đồng hồ' }
    ];

    materials = [
        { value: 'gold', label: 'Vàng' },
        { value: 'silver', label: 'Bạc' },
        { value: 'platinum', label: 'Bạch kim' },
        { value: 'rose-gold', label: 'Vàng hồng' },
        { value: 'stainless-steel', label: 'Thép không gỉ' }
    ];

    colors = [
        { value: 'gold', label: 'Vàng' },
        { value: 'silver', label: 'Bạc' },
        { value: 'rose-gold', label: 'Vàng hồng' },
        { value: 'black', label: 'Đen' },
        { value: 'white', label: 'Trắng' }
    ];

    sizes = [
        { value: 'xs', label: 'XS' },
        { value: 's', label: 'S' },
        { value: 'm', label: 'M' },
        { value: 'l', label: 'L' },
        { value: 'xl', label: 'XL' }
    ];

    displayedColumns: string[] = [
        'image',
        'name',
        'category',
        'price',
        'stock',
        'rating',
        'actions'
    ];

    constructor(
        private dialog: MatDialog,
        private snackBar: MatSnackBar,
        private router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        // Kiểm tra quyền admin trước khi cho phép truy cập
        this.checkAdminPermission();
    }

    private checkAdminPermission() {
        const currentUser = this.authService.getCurrentUser();
        
        if (!currentUser) {
            // Chưa đăng nhập
            this.redirectToHome('Bạn cần đăng nhập để truy cập trang quản trị');
            return;
        }

        // Kiểm tra role admin
        if (!this.isAdmin(currentUser)) {
            // Không có quyền admin
            this.redirectToHome('Bạn không có quyền truy cập trang quản trị');
            return;
        }

        // Có quyền admin, cho phép hiển thị
        this.hasAdminPermission = true;
        this.isCheckingPermission = false;
        this.activeSection = 'dashboard';
    }

    private isAdmin(user: any): boolean {
        if (!user) return false;
        
        // Kiểm tra nếu user có roles array và chứa ROLE_ADMIN
        if (user.roles && Array.isArray(user.roles)) {
            return user.roles.some((role: any) => 
                role.name === 'ROLE_ADMIN' || role === 'ROLE_ADMIN'
            );
        }
        
        // Kiểm tra nếu user có role trực tiếp
        if (user.role === 'ROLE_ADMIN') {
            return true;
        }
        
        return false;
    }

    private redirectToHome(message: string) {
        // Redirect ngay lập tức đến trang 404
        this.router.navigate(['/404']);

    }

    setActiveSection(section: string) {
        this.activeSection = section;
    }

    loadProducts() {
        this.isLoading = true;
        // Mock data - replace with actual API call
        setTimeout(() => {
            this.products = [
                {
                    id: '1',
                    name: 'Nhẫn vàng 18k đính kim cương',
                    description: 'Nhẫn vàng 18k với viên kim cương 0.5ct, thiết kế tinh tế và sang trọng.',
                    price: 15000000,
                    originalPrice: 18000000,
                    category: 'rings',
                    material: 'gold',
                    color: 'gold',
                    size: 'm',
                    images: ['https://via.placeholder.com/300x300?text=Ring+1'],
                    inStock: true,
                    rating: 4.8,
                    reviewCount: 25,
                    tags: ['kim cương', 'vàng 18k', 'sang trọng'],
                    createdAt: new Date('2024-01-15'),
                    updatedAt: new Date('2024-01-15')
                },
                {
                    id: '2',
                    name: 'Dây chuyền bạc 925',
                    description: 'Dây chuyền bạc 925 với thiết kế hiện đại, phù hợp cho mọi dịp.',
                    price: 2500000,
                    category: 'necklaces',
                    material: 'silver',
                    color: 'silver',
                    size: 'm',
                    images: ['https://via.placeholder.com/300x300?text=Necklace+1'],
                    inStock: true,
                    rating: 4.5,
                    reviewCount: 18,
                    tags: ['bạc 925', 'hiện đại'],
                    createdAt: new Date('2024-01-10'),
                    updatedAt: new Date('2024-01-10')
                }
            ];
            this.filteredProducts = [...this.products];
            this.isLoading = false;
        }, 1000);
    }

    filterProducts() {
        this.filteredProducts = this.products.filter(product => {
            const matchesSearch = product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                product.description.toLowerCase().includes(this.searchTerm.toLowerCase());
            const matchesCategory = !this.selectedCategory || product.category === this.selectedCategory;
            return matchesSearch && matchesCategory;
        });
    }

    openAddDialog() {
        this.resetForm();
        this.isAddDialogOpen = true;
    }

    openEditDialog(product: Product) {
        this.editingProduct = product;
        this.productForm = {
            name: product.name,
            description: product.description,
            price: product.price,
            originalPrice: product.originalPrice,
            category: product.category,
            material: product.material,
            color: product.color,
            size: product.size,
            images: [],
            inStock: product.inStock,
            tags: [...product.tags]
        };
        this.previewImages = [...product.images];
        this.isEditDialogOpen = true;
    }

    closeDialogs() {
        this.isAddDialogOpen = false;
        this.isEditDialogOpen = false;
        this.editingProduct = null;
        this.resetForm();
    }

    resetForm() {
        this.productForm = {
            name: '',
            description: '',
            price: 0,
            originalPrice: 0,
            category: '',
            material: '',
            color: '',
            size: '',
            images: [],
            inStock: true,
            tags: []
        };
        this.previewImages = [];
        this.newTag = '';
    }

    onImageSelected(event: any) {
        const files = event.target.files;
        if (files) {
            for (let i = 0; i < files.length; i++) {
                this.productForm.images.push(files[i]);
                const reader = new FileReader();
                reader.onload = (e: any) => {
                    this.previewImages.push(e.target.result);
                };
                reader.readAsDataURL(files[i]);
            }
        }
    }

    removeImage(index: number) {
        this.previewImages.splice(index, 1);
        this.productForm.images.splice(index, 1);
    }

    addTag() {
        if (this.newTag.trim() && !this.productForm.tags.includes(this.newTag.trim())) {
            this.productForm.tags.push(this.newTag.trim());
            this.newTag = '';
        }
    }

    removeTag(tag: string) {
        this.productForm.tags = this.productForm.tags.filter(t => t !== tag);
    }

    saveProduct() {
        if (this.validateForm()) {
            this.isLoading = true;

            // Simulate API call
            setTimeout(() => {
                if (this.isEditDialogOpen && this.editingProduct) {
                    // Update existing product
                    const index = this.products.findIndex(p => p.id === this.editingProduct!.id);
                    if (index !== -1) {
                        this.products[index] = {
                            ...this.products[index],
                            ...this.productForm,
                            images: this.previewImages,
                            updatedAt: new Date()
                        };
                    }
                    this.snackBar.open('Cập nhật sản phẩm thành công!', 'Đóng', { duration: 3000 });
                } else {
                    // Add new product
                    const newProduct: Product = {
                        id: Date.now().toString(),
                        ...this.productForm,
                        images: this.previewImages,
                        rating: 0,
                        reviewCount: 0,
                        createdAt: new Date(),
                        updatedAt: new Date()
                    };
                    this.products.unshift(newProduct);
                    this.snackBar.open('Thêm sản phẩm thành công!', 'Đóng', { duration: 3000 });
                }

                this.filterProducts();
                this.closeDialogs();
                this.isLoading = false;
            }, 1500);
        }
    }

    deleteProduct(product: Product) {
        if (confirm(`Bạn có chắc chắn muốn xóa sản phẩm "${product.name}"?`)) {
            this.isLoading = true;

            // Simulate API call
            setTimeout(() => {
                this.products = this.products.filter(p => p.id !== product.id);
                this.filterProducts();
                this.snackBar.open('Xóa sản phẩm thành công!', 'Đóng', { duration: 3000 });
                this.isLoading = false;
            }, 1000);
        }
    }

    validateForm(): boolean {
        if (!this.productForm.name.trim()) {
            this.snackBar.open('Vui lòng nhập tên sản phẩm!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.productForm.description.trim()) {
            this.snackBar.open('Vui lòng nhập mô tả sản phẩm!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.productForm.price <= 0) {
            this.snackBar.open('Vui lòng nhập giá sản phẩm hợp lệ!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.productForm.category) {
            this.snackBar.open('Vui lòng chọn danh mục!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.previewImages.length === 0) {
            this.snackBar.open('Vui lòng thêm ít nhất một hình ảnh!', 'Đóng', { duration: 3000 });
            return false;
        }
        return true;
    }

    getCategoryLabel(value: string): string {
        return this.categories.find(c => c.value === value)?.label || value;
    }

    getMaterialLabel(value: string): string {
        return this.materials.find(m => m.value === value)?.label || value;
    }

    getColorLabel(value: string): string {
        return this.colors.find(c => c.value === value)?.label || value;
    }

    getSizeLabel(value: string): string {
        return this.sizes.find(s => s.value === value)?.label || value;
    }

    formatPrice(price: number): string {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price);
    }
}
