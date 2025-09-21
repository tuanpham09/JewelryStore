import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CustomerProfileDto {
  id: number;
  email: string;
  fullName: string;
  phoneNumber: string;
  dateOfBirth: string;
  gender: string;
  avatarUrl: string;
  address: string;
  city: string;
  province: string;
  postalCode: string;
  country: string;
  preferredLanguage: string;
  preferredCurrency: string;
  emailVerified: boolean;
  phoneVerified: boolean;
  createdAt: string;
  updatedAt: string;
  lastLoginAt: string;
}

export interface UpdateProfileRequest {
  fullName: string;
  phoneNumber: string;
  dateOfBirth: string;
  gender: string;
  address: string;
  city: string;
  province: string;
  postalCode: string;
  country: string;
  preferredLanguage: string;
  preferredCurrency: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}

export interface UserStats {
  totalOrders: number;
  totalSpent: number;
  memberSince: string;
  lastLogin: string;
}

@Injectable({
  providedIn: 'root'
})
export class CustomerProfileService {
  private apiUrl = `${environment.apiUrl}/customer`;

  constructor(private http: HttpClient) { }

  /**
   * Lấy thông tin profile của user hiện tại
   */
  getProfile(): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile`);
  }

  /**
   * Cập nhật thông tin profile
   */
  updateProfile(profileData: UpdateProfileRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/profile`, profileData);
  }

  /**
   * Đổi mật khẩu
   */
  changePassword(passwordData: ChangePasswordRequest): Observable<any> {
    return this.http.put(`${this.apiUrl}/change-password`, passwordData);
  }

  /**
   * Upload avatar
   */
  uploadAvatar(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('avatar', file);
    return this.http.post(`${this.apiUrl}/upload-avatar`, formData);
  }

  /**
   * Lấy thống kê user
   */
  getUserStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/stats`);
  }

  /**
   * Format ngày tháng
   */
  formatDate(dateString: string): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN');
  }

  /**
   * Format tiền tệ
   */
  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount);
  }

  /**
   * Lấy initials từ tên
   */
  getInitials(fullName: string): string {
    if (!fullName) return 'U';
    return fullName
      .split(' ')
      .map(name => name.charAt(0))
      .join('')
      .toUpperCase()
      .substring(0, 2);
  }

  /**
   * Validate email
   */
  isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * Validate phone number
   */
  isValidPhone(phone: string): boolean {
    const phoneRegex = /^[0-9]{10,11}$/;
    return phoneRegex.test(phone);
  }
}
