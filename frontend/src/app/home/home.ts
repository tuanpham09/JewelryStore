import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  currentUser: any = null;

  constructor(
    private authService: AuthService,
    public router: Router  // Đổi thành public để sử dụng trong template
  ) {}

  ngOnInit() {
    // Load user từ token nếu có
    this.authService.loadUserFromToken();
    
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      // Không redirect về login nữa, cho phép truy cập home mà không cần đăng nhập
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
