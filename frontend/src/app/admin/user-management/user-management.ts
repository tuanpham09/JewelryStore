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
import { AdminService, AdminUserDto } from '../../services/admin.service';

interface UserForm {
    email: string;
    fullName: string;
    phoneNumber: string;
    address: string;
    enabled: boolean;
    roles: string[];
}

@Component({
    selector: 'app-user-management',
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
        MatPaginatorModule
    ],
    templateUrl: './user-management.html',
    styleUrl: './user-management.css'
})
export class UserManagementComponent implements OnInit {
    users: AdminUserDto[] = [];
    filteredUsers: AdminUserDto[] = [];
    searchTerm: string = '';
    isEditDialogOpen: boolean = false;
    editingUser: AdminUserDto | null = null;
    isLoading: boolean = false;

    // Pagination
    pageIndex: number = 0;
    pageSize: number = 20;
    totalUsers: number = 0;

    // Form data
    userForm: UserForm = {
        email: '',
        fullName: '',
        phoneNumber: '',
        address: '',
        enabled: true,
        roles: []
    };

    // Available roles
    availableRoles: string[] = ['ROLE_USER', 'ROLE_STAFF', 'ROLE_ADMIN'];

    // Table columns
    displayedColumns: string[] = ['user', 'email', 'roles', 'status', 'createdAt', 'actions'];

    constructor(
        private adminService: AdminService,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.loadUsers();
    }

    loadUsers() {
        this.isLoading = true;
        console.log('Loading users from API...');
        this.adminService.getUsers(this.pageIndex, this.pageSize).subscribe({
            next: (users) => {
                console.log('Users loaded successfully:', users);
                this.users = users;
                this.filteredUsers = users;
                this.totalUsers = users.length; // Note: This should be total count from API
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading users:', error);
                this.snackBar.open(`Lỗi khi tải danh sách người dùng: ${error.status} ${error.statusText}`, 'Đóng', { duration: 5000 });
                this.isLoading = false;
            }
        });
    }

    filterUsers() {
        this.filteredUsers = this.users.filter(user => {
            const matchesSearch = user.fullName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                user.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
                (user.phoneNumber && user.phoneNumber.includes(this.searchTerm));
            return matchesSearch;
        });
    }

    openEditDialog(user: AdminUserDto) {
        this.editingUser = user;
        this.userForm = {
            email: user.email,
            fullName: user.fullName,
            phoneNumber: user.phoneNumber || '',
            address: user.address || '',
            enabled: user.enabled,
            roles: [...user.roles]
        };
        this.isEditDialogOpen = true;
    }

    closeDialogs() {
        this.isEditDialogOpen = false;
        this.editingUser = null;
        this.resetForm();
    }

    resetForm() {
        this.userForm = {
            email: '',
            fullName: '',
            phoneNumber: '',
            address: '',
            enabled: true,
            roles: []
        };
    }

    saveUser() {
        if (this.validateForm()) {
            this.isLoading = true;

            if (this.isEditDialogOpen && this.editingUser) {
                // Update user roles
                this.adminService.updateUserRoles(this.editingUser.id, this.userForm.roles).subscribe({
                    next: (updatedUser) => {
                        const index = this.users.findIndex(u => u.id === updatedUser.id);
                        if (index !== -1) {
                            this.users[index] = updatedUser;
                        }
                        this.snackBar.open('Cập nhật quyền người dùng thành công!', 'Đóng', { duration: 3000 });
                        this.filterUsers();
                        this.closeDialogs();
                        this.isLoading = false;
                    },
                    error: (error) => {
                        console.error('Error updating user roles:', error);
                        this.snackBar.open('Lỗi khi cập nhật quyền người dùng', 'Đóng', { duration: 3000 });
                        this.isLoading = false;
                    }
                });
            }
        }
    }

    toggleUserStatus(user: AdminUserDto) {
        const newStatus = !user.enabled;
        this.isLoading = true;
        
        this.adminService.updateUserStatus(user.id, newStatus).subscribe({
            next: (updatedUser) => {
                const index = this.users.findIndex(u => u.id === updatedUser.id);
                if (index !== -1) {
                    this.users[index] = updatedUser;
                }
                this.snackBar.open(`${newStatus ? 'Kích hoạt' : 'Vô hiệu hóa'} người dùng thành công!`, 'Đóng', { duration: 3000 });
                this.filterUsers();
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error updating user status:', error);
                this.snackBar.open('Lỗi khi cập nhật trạng thái người dùng', 'Đóng', { duration: 3000 });
                this.isLoading = false;
            }
        });
    }

    deleteUser(user: AdminUserDto) {
        if (confirm(`Bạn có chắc chắn muốn xóa người dùng "${user.fullName}"?`)) {
            this.isLoading = true;
            this.adminService.deleteUser(user.id).subscribe({
                next: () => {
                    this.users = this.users.filter(u => u.id !== user.id);
                    this.snackBar.open('Xóa người dùng thành công!', 'Đóng', { duration: 3000 });
                    this.filterUsers();
                    this.isLoading = false;
                },
                error: (error) => {
                    console.error('Error deleting user:', error);
                    this.snackBar.open('Lỗi khi xóa người dùng', 'Đóng', { duration: 3000 });
                    this.isLoading = false;
                }
            });
        }
    }

    onPageChange(event: PageEvent) {
        this.pageIndex = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadUsers();
    }

    validateForm(): boolean {
        if (!this.userForm.fullName.trim()) {
            this.snackBar.open('Vui lòng nhập tên đầy đủ!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (!this.userForm.email.trim()) {
            this.snackBar.open('Vui lòng nhập email!', 'Đóng', { duration: 3000 });
            return false;
        }
        if (this.userForm.roles.length === 0) {
            this.snackBar.open('Vui lòng chọn ít nhất một quyền!', 'Đóng', { duration: 3000 });
            return false;
        }
        return true;
    }

    formatDate(dateString: string): string {
        return new Date(dateString).toLocaleDateString('vi-VN');
    }

    getRoleDisplayName(role: string): string {
        const roleMap: { [key: string]: string } = {
            'ROLE_USER': 'Người dùng',
            'ROLE_STAFF': 'Nhân viên',
            'ROLE_ADMIN': 'Quản trị viên'
        };
        return roleMap[role] || role;
    }
}
