import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
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
  form = { fullName: '', email: '', password: '', confirm: '' };

  submit() {
    if (this.form.password !== this.form.confirm) {
      alert('Passwords do not match');
      return;
    }
    this.auth
      .register({ fullName: this.form.fullName, email: this.form.email, password: this.form.password })
      .subscribe({
        next: res => alert(res.message),
        error: err => alert(err.error?.message || 'Registration failed')
      });
  }
}

