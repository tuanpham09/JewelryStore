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
import { AuthService } from '../auth.service';
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
        Header,
        Footer
    ],
    templateUrl: './profile.html',
    styleUrl: './profile.css'
})
export class Profile implements OnInit {
    currentUser: any = null;
    selectedTab: number = 0;

    // Expose document to template
    document = document;

    // User Info
    userInfo = {
        fullName: '',
        email: '',
        phone: '',
        dateOfBirth: '',
        gender: '',
        avatar: ''
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
    userStats = {
        totalOrders: 0,
        totalSpent: 0,
        memberSince: '',
        lastLogin: ''
    };

    constructor(
        public router: Router,
        private authService: AuthService,
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
        // Mock data - trong thực tế sẽ gọi API
        this.userInfo = {
            fullName: this.currentUser?.fullName || 'Nguyễn Văn A',
            email: this.currentUser?.email || 'user@example.com',
            phone: '0123456789',
            dateOfBirth: '1990-01-01',
            gender: 'male',
            avatar: ''
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
            // Logic upload avatar
            console.log('Uploading avatar:', file);
        }
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
        if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
            alert('Mật khẩu mới không khớp!');
            return;
        }

        console.log('Changing password:', this.passwordForm);
        // API call để đổi mật khẩu
        this.passwordForm = {
            currentPassword: '',
            newPassword: '',
            confirmPassword: ''
        };
    }

    // Navigation
    goBack() {
        this.router.navigate(['/']);
    }

    getInitials(): string {
        if (this.userInfo.fullName) {
            return this.userInfo.fullName
                .split(' ')
                .map(name => name.charAt(0))
                .join('')
                .toUpperCase()
                .substring(0, 2);
        }
        return 'U';
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
