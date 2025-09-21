import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
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
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AdminService, ProductDto, ProductUpsertDto, CategoryDto } from '../../services/admin.service';
import { environment } from '../../../environments/environment';

interface ProductForm {
    // Basic Info
    name: string;
    slug?: string;
    description: string;
    shortDescription?: string;
    price: number;
    originalPrice?: number;
    salePrice?: number;
    categoryId: number;
    
    // Physical Properties
    material: string;
    color: string;
    size: string; // Single size
    sizes: string[]; // Multi-select for sizes
    weight?: number;
    dimensions?: string;
    
    // Business Info
    sku?: string;
    barcode?: string;
    brand?: string;
    origin?: string;
    warrantyPeriod?: number;
    
    // Stock & Inventory
    stock: number;
    minStock?: number;
    maxStock?: number;
    
    // Status Flags
    isActive: boolean;
    isFeatured: boolean;
    isNew: boolean;
    isBestseller: boolean;
    
    // Analytics & Tracking
    viewCount?: number;
    soldCount?: number;
    
    // SEO
    metaTitle?: string;
    metaDescription?: string;
    metaKeywords?: string;
    
    // Media
    images: File[];
    thumbnail?: string;
    
    // Tags
    tags: string[];
}

@Component({
    selector: 'app-product-management',
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
        MatCheckboxModule
    ],
    templateUrl: './product-management.html',
    styleUrl: './product-management.css'
})
export class ProductManagementComponent implements OnInit {
    // Expose Math object to template
    Math = Math;

    products: ProductDto[] = [];
    filteredProducts: ProductDto[] = [];
    categories: CategoryDto[] = [];
    searchTerm: string = '';
    selectedCategory: number | null = null;
    isAddDialogOpen: boolean = false;
    isEditDialogOpen: boolean = false;
    editingProduct: ProductDto | null = null;
    isLoading: boolean = false;

    // Additional form properties
    discountPercentage: number = 0;
    availableTags: string[] = ['Trang sức', 'Nhẫn', 'Dây chuyền', 'Vòng tay', 'Bông tai', 'Lắc tay'];
    slug: string = '';

    // Form data
    productForm: ProductForm = {
        // Basic Info
        name: '',
        slug: '',
        description: '',
        shortDescription: '',
        price: 0,
        originalPrice: 0,
        salePrice: 0,
        categoryId: 0,
        
        // Physical Properties
        material: '',
        color: '',
        size: '',
        sizes: [],
        weight: 0,
        dimensions: '',
        
        // Business Info
        sku: '',
        barcode: '',
        brand: '',
        origin: '',
        warrantyPeriod: 0,
        
        // Stock & Inventory
        stock: 0,
        minStock: 5,
        maxStock: 1000,
        
        // Status Flags
        isActive: true,
        isFeatured: false,
        isNew: true,
        isBestseller: false,
        
        // Analytics & Tracking
        viewCount: 0,
        soldCount: 0,
        
        // SEO
        metaTitle: '',
        metaDescription: '',
        metaKeywords: '',
        
        // Media
        images: [],
        thumbnail: '',
        
        // Tags
        tags: []
    };

    newTag: string = '';
    previewImages: string[] = [];

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
        { value: 'xl', label: 'XL' },
        { value: 'xxl', label: 'XXL' },
        { value: 'xxxl', label: 'XXXL' },
        { value: 'free-size', label: 'Free Size' },
        { value: 'one-size', label: 'One Size' }
    ];

    sizeOptions = [
        'XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL', 'Free Size', 'One Size'
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
        private adminService: AdminService
    ) { }

    ngOnInit() {
        this.testApiConnection();
        this.loadCategories();
        this.loadProducts();
    }

    // Generate slug from product name
    generateSlug(name: string): string {
        return name
            .toLowerCase()
            .trim()
            .replace(/[àáạảãâầấậẩẫăằắặẳẵ]/g, 'a')
            .replace(/[èéẹẻẽêềếệểễ]/g, 'e')
            .replace(/[ìíịỉĩ]/g, 'i')
            .replace(/[òóọỏõôồốộổỗơờớợởỡ]/g, 'o')
            .replace(/[ùúụủũưừứựửữ]/g, 'u')
            .replace(/[ỳýỵỷỹ]/g, 'y')
            .replace(/đ/g, 'd')
            .replace(/[^a-z0-9\s-]/g, '')
            .replace(/\s+/g, '-')
            .replace(/-+/g, '-')
            .replace(/^-|-$/g, '');
    }

    // Auto-generate slug when name changes
    onNameChange() {
        if (this.productForm.name) {
            this.productForm.slug = this.generateSlug(this.productForm.name);
        }
    }

    // Upload images for product
    uploadImages(productId: number) {
        if (this.productForm.images && this.productForm.images.length > 0) {
            console.log(`Uploading ${this.productForm.images.length} new images for product ${productId}`);
            this.productForm.images.forEach((file: File, index: number) => {
                console.log(`Uploading image ${index + 1}:`, file.name);
                this.adminService.uploadProductImage(productId, file).subscribe({
                    next: (result) => {
                        console.log(`Image ${index + 1} uploaded successfully:`, result);
                        this.snackBar.open(`Ảnh ${index + 1} đã upload thành công!`, 'Đóng', { duration: 2000 });
                    },
                    error: (error) => {
                        console.error(`Error uploading image ${index + 1}:`, error);
                        this.snackBar.open(`Lỗi khi upload ảnh ${index + 1}: ${error.error?.message || error.message}`, 'Đóng', { duration: 5000 });
                    }
                });
            });
        } else {
            console.log('No new images to upload');
        }
    }

    testApiConnection() {
        console.log('Testing API connection...');
        console.log('API Base URL:', `${environment.apiUrl}/admin`);
        console.log('Products endpoint:', `${environment.apiUrl}/admin/products`);
        console.log('Categories endpoint:', `${environment.apiUrl}/admin/categories`);
    }

    loadCategories() {
        // Don't load if already loaded
        if (this.categories.length > 0) {
            return;
        }
        
        console.log('Loading categories from API...');
        this.adminService.getCategories().subscribe({
            next: (categories) => {
                console.log('Categories loaded successfully:', categories);
                this.categories = categories;
            },
            error: (error) => {
                console.error('Error loading categories:', error);
                console.error('Error details:', {
                    status: error.status,
                    statusText: error.statusText,
                    message: error.message,
                    url: error.url
                });
                this.snackBar.open(`Lỗi khi tải danh mục: ${error.status} ${error.statusText}`, 'Đóng', { duration: 5000 });
            }
        });
    }

    loadProducts() {
        this.isLoading = true;
        console.log('Loading products from API...');
        this.adminService.getProducts().subscribe({
            next: (products) => {
                console.log('Products loaded successfully:', products);
                this.products = products;
                this.filteredProducts = [...this.products];
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading products:', error);
                console.error('Error details:', {
                    status: error.status,
                    statusText: error.statusText,
                    message: error.message,
                    url: error.url
                });
                
                // Fallback to empty array if API fails
                this.products = [];
                this.filteredProducts = [];
                this.isLoading = false;
                
                this.snackBar.open(`Lỗi khi tải danh sách sản phẩm: ${error.status} ${error.statusText}`, 'Đóng', { duration: 5000 });
            }
        });
    }

    filterProducts() {
        this.filteredProducts = this.products.filter(product => {
            const matchesSearch = product.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                (product.description && product.description.toLowerCase().includes(this.searchTerm.toLowerCase()));
            const matchesCategory = !this.selectedCategory || product.categoryId === this.selectedCategory;
            return matchesSearch && matchesCategory;
        });
    }

    openAddDialog() {
        this.resetForm();
        this.isAddDialogOpen = true;
    }

    openEditDialog(product: ProductDto) {
        this.editingProduct = product;
        
        // Ensure categories are loaded
        if (this.categories.length === 0) {
            this.loadCategories();
        }
        
        this.productForm = {
            // Basic Info
            name: product.name,
            slug: product.slug || this.generateSlug(product.name),
            description: product.description || '',
            shortDescription: product.shortDescription || '',
            price: product.price,
            originalPrice: product.originalPrice,
            salePrice: product.salePrice,
            categoryId: product.categoryId || 0,
            
            // Physical Properties
            material: product.material || '',
            color: product.color || '',
            size: product.size || '',
            sizes: product.sizes || [],
            weight: product.weight || 0,
            dimensions: product.dimensions || '',
            
            // Business Info
            sku: product.sku || '',
            barcode: product.barcode || '',
            brand: product.brand || '',
            origin: product.origin || '',
            warrantyPeriod: product.warrantyPeriod || 0,
            
            // Stock & Inventory
            stock: product.stock || 0,
            minStock: product.minStock || 5,
            maxStock: product.maxStock || 1000,
            
            // Status Flags
            isActive: product.isActive !== false,
            isFeatured: product.isFeatured || false,
            isNew: product.isNew || false,
            isBestseller: product.isBestseller || false,
            
            // Analytics & Tracking
            viewCount: product.viewCount || 0,
            soldCount: product.soldCount || 0,
            
            // SEO
            metaTitle: product.metaTitle || '',
            metaDescription: product.metaDescription || '',
            metaKeywords: product.metaKeywords || '',
            
            // Media
            images: [],
            thumbnail: product.thumbnail || '',
            
            // Tags
            tags: product.tags || []
        };
        this.previewImages = product.images ? product.images.map(img => img.imageUrl) : [];
        // Wait for categories to load before opening dialog
        if (this.categories.length === 0) {
            this.loadCategories();
            // Wait a bit for categories to load
            setTimeout(() => {
                this.isEditDialogOpen = true;
            }, 100);
        } else {
            this.isEditDialogOpen = true;
        }
    }

    closeDialogs() {
        this.isAddDialogOpen = false;
        this.isEditDialogOpen = false;
        this.editingProduct = null;
        this.resetForm();
    }

    resetForm() {
        this.productForm = {
            // Basic Info
            name: '',
            slug: '',
            description: '',
            shortDescription: '',
            price: 0,
            originalPrice: 0,
            salePrice: 0,
            categoryId: 0,
            
            // Physical Properties
            material: '',
            color: '',
            size: '',
            sizes: [],
            weight: 0,
            dimensions: '',
            
            // Business Info
            sku: '',
            barcode: '',
            brand: '',
            origin: '',
            warrantyPeriod: 0,
            
            // Stock & Inventory
            stock: 0,
            minStock: 5,
            maxStock: 1000,
            
            // Status Flags
            isActive: true,
            isFeatured: false,
            isNew: true,
            isBestseller: false,
            
            // Analytics & Tracking
            viewCount: 0,
            soldCount: 0,
            
            // SEO
            metaTitle: '',
            metaDescription: '',
            metaKeywords: '',
            
            // Media
            images: [],
            thumbnail: '',
            
            // Tags
            tags: []
        };
        this.previewImages = [];
        this.newTag = '';
    }

    onImageSelected(event: any) {
        const files = event.target.files;
        if (files) {
            console.log(`Selected ${files.length} new images`);
            for (let i = 0; i < files.length; i++) {
                // Add to productForm.images (for upload)
                this.productForm.images.push(files[i]);
                
                // Add to previewImages (for display)
                const reader = new FileReader();
                reader.onload = (e: any) => {
                    this.previewImages.push(e.target.result);
                };
                reader.readAsDataURL(files[i]);
            }
            console.log(`Total images in form: ${this.productForm.images.length}`);
            console.log(`Total preview images: ${this.previewImages.length}`);
        }
    }

    removeImage(index: number) {
        console.log(`Removing image at index ${index}`);
        console.log(`Before removal - previewImages: ${this.previewImages.length}, productForm.images: ${this.productForm.images.length}`);
        
        // Remove from preview
        this.previewImages.splice(index, 1);
        
        // Only remove from productForm.images if it's a new image (File object)
        // If it's an old image (string URL), don't remove from productForm.images
        if (index < this.productForm.images.length) {
            this.productForm.images.splice(index, 1);
        }
        
        console.log(`After removal - previewImages: ${this.previewImages.length}, productForm.images: ${this.productForm.images.length}`);
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

            const productData: ProductUpsertDto = {
                name: this.productForm.name,
                slug: this.productForm.slug || this.generateSlug(this.productForm.name),
                description: this.productForm.description,
                shortDescription: this.productForm.shortDescription,
                price: this.productForm.price,
                originalPrice: this.productForm.originalPrice,
                salePrice: this.productForm.salePrice,
                categoryId: this.productForm.categoryId,
                material: this.productForm.material,
                color: this.productForm.color,
                size: this.productForm.size || '',
                sizes: this.productForm.sizes,
                weight: this.productForm.weight,
                dimensions: this.productForm.dimensions,
                sku: this.productForm.sku,
                barcode: this.productForm.barcode,
                brand: this.productForm.brand,
                origin: this.productForm.origin,
                warrantyPeriod: this.productForm.warrantyPeriod,
                stock: this.productForm.stock,
                minStock: this.productForm.minStock,
                maxStock: this.productForm.maxStock,
                inStock: this.productForm.stock > 0,
                isActive: this.productForm.isActive,
                isFeatured: this.productForm.isFeatured,
                isNew: this.productForm.isNew,
                isBestseller: this.productForm.isBestseller,
                viewCount: this.productForm.viewCount,
                soldCount: this.productForm.soldCount,
                metaTitle: this.productForm.metaTitle,
                metaDescription: this.productForm.metaDescription,
                metaKeywords: this.productForm.metaKeywords,
                tags: this.productForm.tags
            };

            if (this.isEditDialogOpen && this.editingProduct) {
                // Update existing product
                this.adminService.updateProduct(this.editingProduct.id, productData).subscribe({
                    next: (updatedProduct) => {
                        const index = this.products.findIndex(p => p.id === updatedProduct.id);
                        if (index !== -1) {
                            this.products[index] = updatedProduct;
                        }
                        
                        // Upload images if any
                        this.uploadImages(updatedProduct.id);
                        
                        this.snackBar.open('Cập nhật sản phẩm thành công!', 'Đóng', { duration: 3000 });
                        this.filterProducts();
                        this.closeDialogs();
                        this.isLoading = false;
                    },
                    error: (error) => {
                        console.error('Error updating product:', error);
                        this.snackBar.open('Lỗi khi cập nhật sản phẩm', 'Đóng', { duration: 3000 });
                        this.isLoading = false;
                    }
                });
            } else {
                // Add new product
                this.adminService.createProduct(productData).subscribe({
                    next: (newProduct) => {
                        this.products.unshift(newProduct);
                        
                        // Upload images if any
                        this.uploadImages(newProduct.id);
                        
                        this.snackBar.open('Thêm sản phẩm thành công!', 'Đóng', { duration: 3000 });
                        this.filterProducts();
                        this.closeDialogs();
                        this.isLoading = false;
                    },
                    error: (error) => {
                        console.error('Error creating product:', error);
                        this.snackBar.open('Lỗi khi thêm sản phẩm', 'Đóng', { duration: 3000 });
                        this.isLoading = false;
                    }
                });
            }
        }
    }

    deleteProduct(product: ProductDto) {
        if (confirm(`Bạn có chắc chắn muốn xóa sản phẩm "${product.name}"?`)) {
            this.isLoading = true;

            this.adminService.deleteProduct(product.id).subscribe({
                next: () => {
                    this.products = this.products.filter(p => p.id !== product.id);
                    this.filterProducts();
                    this.snackBar.open('Xóa sản phẩm thành công!', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error deleting product:', error);
                    this.snackBar.open('Lỗi khi xóa sản phẩm', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
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
        if (!this.productForm.categoryId || this.productForm.categoryId === 0) {
            this.snackBar.open('Vui lòng chọn danh mục!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.productForm.stock < 0) {
            this.snackBar.open('Số lượng tồn kho không hợp lệ!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.productForm.minStock && this.productForm.maxStock && this.productForm.minStock > this.productForm.maxStock) {
            this.snackBar.open('Tồn tối thiểu không được lớn hơn tồn tối đa!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.productForm.metaTitle && this.productForm.metaTitle.length > 60) {
            this.snackBar.open('Meta Title không được vượt quá 60 ký tự!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.productForm.metaDescription && this.productForm.metaDescription.length > 160) {
            this.snackBar.open('Meta Description không được vượt quá 160 ký tự!', 'Đóng', { duration: 3000 });
            return false;
        }
        return true;
    }

    getCategoryLabel(categoryId: number): string {
        return this.categories.find(c => c.id === categoryId)?.name || 'Chưa phân loại';
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
