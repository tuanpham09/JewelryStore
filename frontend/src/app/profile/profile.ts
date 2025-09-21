import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatDividerModule } from '@angular/material/divider';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { AuthService } from '../auth.service';
import { CustomerProfileService, CustomerProfileDto, UpdateProfileRequest, ChangePasswordRequest, UserStats } from '../services/customer-profile.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface Address {
    id: string;
    type: 'home' | 'work' | 'other';
    name: string;
    phone: string;
    address: string;
    city: string;
    district: string;
    ward: string;
    isDefault: boolean;
}

interface UserPreferences {
    language: string;
    currency: string;
    emailNotifications: boolean;
    smsNotifications: boolean;
    newsletter: boolean;
}

@Component({
    selector: 'app-profile',
    imports: [
        CommonModule,
        FormsModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatTabsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        MatDividerModule,
        MatChipsModule,
        MatSlideToggleModule,
        Header,
        Footer
    ],
    templateUrl: './profile.html',
    styleUrl: './profile.css'
})
export class Profile implements OnInit {
    currentUser: any = null;
    selectedTab: number = 0;
    isLoading = false;
    error: string | null = null;

    // Expose document to template
    document = document;

    // User Info - sử dụng CustomerProfileDto
    userInfo: CustomerProfileDto = {
        id: 0,
        email: '',
        fullName: '',
        phoneNumber: '',
        dateOfBirth: '',
        gender: '',
        avatarUrl: '',
        address: '',
        city: '',
        province: '',
        postalCode: '',
        country: '',
        preferredLanguage: 'vi',
        preferredCurrency: 'VND',
        emailVerified: false,
        phoneVerified: false,
        createdAt: '',
        updatedAt: '',
        lastLoginAt: ''
    };

    // Address Management
    addresses: Address[] = [];
    newAddress: Address = {
        id: '',
        type: 'home',
        name: '',
        phone: '',
        address: '',
        city: '',
        district: '',
        ward: '',
        isDefault: false
    };
    showAddressForm: boolean = false;
    editingAddress: Address | null = null;

    // Password Change
    passwordForm = {
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    };

    // Preferences
    preferences: UserPreferences = {
        language: 'vi',
        currency: 'VND',
        emailNotifications: true,
        smsNotifications: true,
        newsletter: true
    };

    // Statistics
    userStats: UserStats = {
        totalOrders: 0,
        totalSpent: 0,
        memberSince: '',
        lastLogin: ''
    };

    constructor(
        public router: Router,
        private authService: AuthService,
        private customerProfileService: CustomerProfileService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
            if (user) {
                this.loadUserData();
            }
        });
    }

    loadUserData() {
        this.isLoading = true;
        this.error = null;

        console.log('Loading user profile from API...');
        
        // Load profile data
        this.customerProfileService.getProfile().subscribe({
            next: (response) => {
                console.log('Profile loaded:', response);
                if (response.success) {
                    this.userInfo = response.data;
                } else {
                    this.error = response.message || 'Không thể tải thông tin profile';
                }
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading profile:', error);
                this.error = 'Có lỗi xảy ra khi tải thông tin profile';
                this.isLoading = false;
                
                // Fallback to mock data for development
                this.loadMockUserData();
            }
        });

        // Load user stats
        this.customerProfileService.getUserStats().subscribe({
            next: (response) => {
                if (response.success) {
                    this.userStats = response.data;
                }
            },
            error: (error) => {
                console.error('Error loading user stats:', error);
                // Fallback to mock stats
                this.loadMockStats();
            }
        });
    }

    private loadMockUserData() {
        // Mock data - fallback khi API không hoạt động
        this.userInfo = {
            id: 1,
            email: this.currentUser?.email || 'user@example.com',
            fullName: this.currentUser?.fullName || 'Nguyễn Văn A',
            phoneNumber: '0123456789',
            dateOfBirth: '1990-01-01',
            gender: 'male',
            avatarUrl: '',
            address: '123 Đường ABC',
            city: 'TP. Hồ Chí Minh',
            province: 'TP. Hồ Chí Minh',
            postalCode: '700000',
            country: 'Việt Nam',
            preferredLanguage: 'vi',
            preferredCurrency: 'VND',
            emailVerified: true,
            phoneVerified: false,
            createdAt: '2023-01-15T00:00:00Z',
            updatedAt: '2024-01-20T00:00:00Z',
            lastLoginAt: '2024-01-20T10:00:00Z'
        };
    }

    private loadMockStats() {
        this.userStats = {
            totalOrders: 12,
            totalSpent: 15400000,
            memberSince: '2023-01-15',
            lastLogin: '2024-01-20'
        };

        this.addresses = [
            {
                id: '1',
                type: 'home',
                name: 'Nguyễn Văn A',
                phone: '0123456789',
                address: '123 Đường ABC',
                city: 'TP. Hồ Chí Minh',
                district: 'Quận 1',
                ward: 'Phường Bến Nghé',
                isDefault: true
            },
            {
                id: '2',
                type: 'work',
                name: 'Nguyễn Văn A',
                phone: '0123456789',
                address: '456 Đường XYZ',
                city: 'TP. Hồ Chí Minh',
                district: 'Quận 3',
                ward: 'Phường Võ Thị Sáu',
                isDefault: false
            }
        ];

        this.userStats = {
            totalOrders: 12,
            totalSpent: 15400000,
            memberSince: '2023-01-15',
            lastLogin: '2024-01-20'
        };
    }


    uploadAvatar(event: any) {
        const file = event.target.files[0];
        if (file) {
            console.log('Uploading avatar:', file);
            
            this.customerProfileService.uploadAvatar(file).subscribe({
                next: (response) => {
                    if (response.success) {
                        this.userInfo.avatarUrl = response.data.avatarUrl;
                        this.snackBar.open('Ảnh đại diện đã được cập nhật', 'Đóng', {
                            duration: 3000
                        });
                    } else {
                        this.snackBar.open('Không thể upload ảnh: ' + response.message, 'Đóng', {
                            duration: 5000
                        });
                    }
                },
                error: (error) => {
                    console.error('Error uploading avatar:', error);
                    this.snackBar.open('Có lỗi xảy ra khi upload ảnh', 'Đóng', {
                        duration: 3000
                    });
                }
            });
        }
    }

    updateUserInfo() {
        if (!this.validateUserInfo()) {
            return;
        }

        const updateData: UpdateProfileRequest = {
            fullName: this.userInfo.fullName,
            phoneNumber: this.userInfo.phoneNumber,
            dateOfBirth: this.userInfo.dateOfBirth,
            gender: this.userInfo.gender,
            address: this.userInfo.address,
            city: this.userInfo.city,
            province: this.userInfo.province,
            postalCode: this.userInfo.postalCode,
            country: this.userInfo.country,
            preferredLanguage: this.userInfo.preferredLanguage,
            preferredCurrency: this.userInfo.preferredCurrency
        };

        console.log('Updating user info:', updateData);

        this.customerProfileService.updateProfile(updateData).subscribe({
            next: (response) => {
                if (response.success) {
                    this.snackBar.open('Thông tin đã được cập nhật thành công', 'Đóng', {
                        duration: 3000
                    });
                    // Reload user data to get updated info
                    this.loadUserData();
                } else {
                    this.snackBar.open('Không thể cập nhật thông tin: ' + response.message, 'Đóng', {
                        duration: 5000
                    });
                }
            },
            error: (error) => {
                console.error('Error updating profile:', error);
                this.snackBar.open('Có lỗi xảy ra khi cập nhật thông tin', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    private validateUserInfo(): boolean {
        if (!this.userInfo.fullName.trim()) {
            this.snackBar.open('Vui lòng nhập họ và tên', 'Đóng', { duration: 3000 });
            return false;
        }

        if (!this.customerProfileService.isValidEmail(this.userInfo.email)) {
            this.snackBar.open('Email không hợp lệ', 'Đóng', { duration: 3000 });
            return false;
        }

        if (this.userInfo.phoneNumber && !this.customerProfileService.isValidPhone(this.userInfo.phoneNumber)) {
            this.snackBar.open('Số điện thoại không hợp lệ', 'Đóng', { duration: 3000 });
            return false;
        }

        return true;
    }

    // Address Methods
    addAddress() {
        if (this.editingAddress) {
            // Update existing address
            const index = this.addresses.findIndex(addr => addr.id === this.editingAddress!.id);
            if (index !== -1) {
                this.addresses[index] = { ...this.newAddress, id: this.editingAddress.id };
            }
        } else {
            // Add new address
            this.newAddress.id = Date.now().toString();
            this.addresses.push({ ...this.newAddress });
        }

        this.resetAddressForm();
    }

    editAddress(address: Address) {
        this.editingAddress = address;
        this.newAddress = { ...address };
        this.showAddressForm = true;
    }

    deleteAddress(addressId: string) {
        this.addresses = this.addresses.filter(addr => addr.id !== addressId);
    }

    setDefaultAddress(addressId: string) {
        this.addresses.forEach(addr => {
            addr.isDefault = addr.id === addressId;
        });
    }

    resetAddressForm() {
        this.newAddress = {
            id: '',
            type: 'home',
            name: '',
            phone: '',
            address: '',
            city: '',
            district: '',
            ward: '',
            isDefault: false
        };
        this.showAddressForm = false;
        this.editingAddress = null;
    }

    // Password Methods
    changePassword() {
        if (!this.passwordForm.currentPassword.trim()) {
            this.snackBar.open('Vui lòng nhập mật khẩu hiện tại', 'Đóng', { duration: 3000 });
            return;
        }

        if (!this.passwordForm.newPassword.trim()) {
            this.snackBar.open('Vui lòng nhập mật khẩu mới', 'Đóng', { duration: 3000 });
            return;
        }

        if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
            this.snackBar.open('Mật khẩu mới không khớp!', 'Đóng', { duration: 3000 });
            return;
        }

        if (this.passwordForm.newPassword.length < 6) {
            this.snackBar.open('Mật khẩu mới phải có ít nhất 6 ký tự', 'Đóng', { duration: 3000 });
            return;
        }

        const passwordData: ChangePasswordRequest = {
            currentPassword: this.passwordForm.currentPassword,
            newPassword: this.passwordForm.newPassword
        };

        console.log('Changing password...');

        this.customerProfileService.changePassword(passwordData).subscribe({
            next: (response) => {
                if (response.success) {
                    this.snackBar.open('Mật khẩu đã được thay đổi thành công', 'Đóng', {
                        duration: 3000
                    });
                    // Reset form
                    this.passwordForm = {
                        currentPassword: '',
                        newPassword: '',
                        confirmPassword: ''
                    };
                } else {
                    this.snackBar.open('Không thể đổi mật khẩu: ' + response.message, 'Đóng', {
                        duration: 5000
                    });
                }
            },
            error: (error) => {
                console.error('Error changing password:', error);
                this.snackBar.open('Có lỗi xảy ra khi đổi mật khẩu', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    // Navigation
    goBack() {
        this.router.navigate(['/']);
    }

    getInitials(): string {
        return this.customerProfileService.getInitials(this.userInfo.fullName);
    }

    formatDate(dateString: string): string {
        return this.customerProfileService.formatDate(dateString);
    }

    formatCurrency(amount: number): string {
        return this.customerProfileService.formatCurrency(amount);
    }

    getAddressTypeLabel(type: string): string {
        switch (type) {
            case 'home': return 'Nhà riêng';
            case 'work': return 'Công ty';
            case 'other': return 'Khác';
            default: return type;
        }
    }

    updatePreferences() {
        console.log('Updating preferences:', this.preferences);
        // TODO: Implement API call to update preferences
        this.snackBar.open('Cài đặt đã được lưu', 'Đóng', {
            duration: 3000
        });
    }
}
