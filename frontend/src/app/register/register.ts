import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  form = { email: '', password: '', confirm: '' };

  submit() {
    if (this.form.password !== this.form.confirm) {
      alert('Passwords do not match');
      return;
    }
    console.log('Register', this.form);
  }
}

