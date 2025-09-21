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
import { AdminService, CategoryDto } from '../../services/admin.service';

interface CategoryForm {
    name: string;
    slug: string;
    description: string;
    imageUrl: string;
    isActive: boolean;
    sortOrder: number;
}

@Component({
    selector: 'app-category-management',
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
        MatTooltipModule
    ],
    templateUrl: './category-management.html',
    styleUrl: './category-management.css'
})
export class CategoryManagementComponent implements OnInit {
    categories: CategoryDto[] = [];
    filteredCategories: CategoryDto[] = [];
    searchTerm: string = '';
    isAddDialogOpen: boolean = false;
    isEditDialogOpen: boolean = false;
    editingCategory: CategoryDto | null = null;
    isLoading: boolean = false;

    // Form data
    categoryForm: CategoryForm = {
        name: '',
        slug: '',
        description: '',
        imageUrl: '',
        isActive: true,
        sortOrder: 0
    };

    // Table columns
    displayedColumns: string[] = ['name', 'description', 'isActive', 'sortOrder', 'actions'];

    constructor(
        private adminService: AdminService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.loadCategories();
    }

    loadCategories() {
        this.isLoading = true;
        console.log('Loading categories from API...');
        this.adminService.getCategories().subscribe({
            next: (categories) => {
                console.log('Categories loaded successfully:', categories);
                this.categories = categories;
                this.filteredCategories = categories; // ✅ Thêm dòng này
                this.isLoading = false;
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
                this.isLoading = false;
            }
        });
    }

    filterCategories() {
        this.filteredCategories = this.categories.filter(category => {
            const matchesSearch = category.name.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                (category.description && category.description.toLowerCase().includes(this.searchTerm.toLowerCase()));
            return matchesSearch;
        });
    }

    openAddDialog() {
        this.resetForm();
        this.isAddDialogOpen = true;
    }

    openEditDialog(category: CategoryDto) {
        this.editingCategory = category;
        this.categoryForm = {
            name: category.name,
            slug: category.slug || this.generateSlug(category.name),
            description: category.description || '',
            imageUrl: category.imageUrl || '',
            isActive: category.isActive !== false,
            sortOrder: category.sortOrder || 0
        };
        this.isEditDialogOpen = true;
    }

    closeDialogs() {
        this.isAddDialogOpen = false;
        this.isEditDialogOpen = false;
        this.editingCategory = null;
        this.resetForm();
    }

    resetForm() {
        this.categoryForm = {
            name: '',
            slug: '',
            description: '',
            imageUrl: '',
            isActive: true,
            sortOrder: 0
        };
    }

    // Generate slug from category name
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
        if (this.categoryForm.name) {
            this.categoryForm.slug = this.generateSlug(this.categoryForm.name);
        }
    }

    saveCategory() {
        if (this.validateForm()) {
            this.isLoading = true;

            const categoryData: CategoryDto = {
                id: this.editingCategory?.id || 0,
                name: this.categoryForm.name,
                slug: this.categoryForm.slug || this.generateSlug(this.categoryForm.name),
                description: this.categoryForm.description,
                imageUrl: this.categoryForm.imageUrl,
                isActive: this.categoryForm.isActive,
                sortOrder: this.categoryForm.sortOrder,
                createdAt: this.editingCategory?.createdAt || new Date().toISOString(),
                updatedAt: new Date().toISOString()
            };

            if (this.isEditDialogOpen && this.editingCategory) {
                // Update existing category
                this.adminService.updateCategory(this.editingCategory.id, categoryData).subscribe({
                    next: (updatedCategory) => {
                        const index = this.categories.findIndex(c => c.id === updatedCategory.id);
                        if (index !== -1) {
                            this.categories[index] = updatedCategory;
                        }
                        this.snackBar.open('Cập nhật danh mục thành công!', 'Đóng', { duration: 3000 });
                        this.filterCategories();
                        this.closeDialogs();
                        this.isLoading = false;
                    },
                    error: (error) => {
                        console.error('Error updating category:', error);
                        this.snackBar.open('Lỗi khi cập nhật danh mục', 'Đóng', { duration: 3000 });
                        this.isLoading = false;
                    }
                });
            } else {
                // Create new category
                this.adminService.createCategory(categoryData).subscribe({
                    next: (newCategory) => {
                        this.categories.unshift(newCategory);
                        this.snackBar.open('Thêm danh mục thành công!', 'Đóng', { duration: 3000 });
                        this.filterCategories();
                        this.closeDialogs();
                        this.isLoading = false;
                    },
                    error: (error) => {
                        console.error('Error creating category:', error);
                        this.snackBar.open('Lỗi khi thêm danh mục', 'Đóng', { duration: 3000 });
                        this.isLoading = false;
                    }
                });
            }
        }
    }

    deleteCategory(category: CategoryDto) {
        if (confirm(`Bạn có chắc chắn muốn xóa danh mục "${category.name}"?`)) {
            this.isLoading = true;
            this.adminService.deleteCategory(category.id).subscribe({
                next: () => {
                    this.categories = this.categories.filter(c => c.id !== category.id);
                    this.snackBar.open('Xóa danh mục thành công!', 'Đóng', { duration: 3000 });
                    this.filterCategories();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error deleting category:', error);
                    this.snackBar.open('Lỗi khi xóa danh mục', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    validateForm(): boolean {
        if (!this.categoryForm.name.trim()) {
            this.snackBar.open('Vui lòng nhập tên danh mục!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.categoryForm.description.trim()) {
            this.snackBar.open('Vui lòng nhập mô tả danh mục!', 'Đóng', { duration: 3000 });
            return false;
        }
        return true;
    }
}
