import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);
  private toastr = inject(ToastrService);

  credentials = { email: '', password: '' };
  error = '';

  ngOnInit() {
    // Kiểm tra nếu đã đăng nhập thì chuyển về home
    this.auth.currentUser$.subscribe(user => {
      if (user) {
        this.handleLoginSuccess();
      }
    });

    // Load user từ token nếu có
    this.auth.loadUserFromToken();
  }

  private handleLoginSuccess() {
    // Kiểm tra callback URL
    const callbackUrl = localStorage.getItem('callback_url');
    if (callbackUrl) {
      localStorage.removeItem('callback_url');
      this.router.navigateByUrl(callbackUrl);
    } else {
      this.router.navigate(['/home']);
    }
  }

  submit() {
    this.error = '';
    console.log('Login form submitted with:', this.credentials);

    this.auth.login(this.credentials).subscribe({
      next: (res) => {
        console.log('Login success response in component:', res);
        if (res.success) {
          this.toastr.success('Đăng nhập thành công!', 'Thành công');
          this.handleLoginSuccess();
        } else {
          this.error = res.message || 'Login failed';
          this.toastr.error(this.error, 'Lỗi đăng nhập');
        }
      },
      error: (err) => {
        console.error('Login error in component:', err);
        if (err.status === 0) {
          this.error = 'Cannot connect to server. Please check if backend is running.';
        } else {
          this.error = err.error?.message || `Login failed (${err.status})`;
        }
        this.toastr.error(this.error, 'Lỗi đăng nhập');
      }
    });
  }
}

