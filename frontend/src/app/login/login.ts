import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  private auth = inject(AuthService);
  credentials = { email: '', password: '' };

  submit() {
    this.auth.login(this.credentials).subscribe({
      next: res => {
        alert(res.message);
        localStorage.setItem('token', res.data.token);
      },
      error: err => alert(err.error?.message || 'Login failed')
    });
  }
}

