import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  userData = {
    email: '',
    password: '',
    confirmPassword: '',
    fullName: ''
  };
  error = '';

  submit() {
    if (this.userData.password !== this.userData.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    console.log('Register form submitted with:', {...this.userData, password: '***'});
    
    // Create a copy without confirmPassword
    const registerData = {
      email: this.userData.email,
      password: this.userData.password,
      fullName: this.userData.fullName
    };

    this.auth.register(registerData).subscribe({
      next: (res) => {
        console.log('Register success response in component:', res);
        if (res.success) {
          this.router.navigate(['/']);
        } else {
          this.error = res.message || 'Registration failed';
        }
      },
      error: (err) => {
        console.error('Register error in component:', err);
        if (err.status === 0) {
          this.error = 'Cannot connect to server. Please check if backend is running.';
        } else {
          this.error = err.error?.message || `Registration failed (${err.status})`;
        }
      }
    });
  }
}

