import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

@Component({
    selector: 'app-not-found',
    standalone: true,
    imports: [
        CommonModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        Header,
        Footer
    ],
    templateUrl: './not-found.html',
    styleUrl: './not-found.css'
})
export class NotFoundComponent {
    constructor(private router: Router) { }

    goHome() {
        this.router.navigate(['/']);
    }

    goBack() {
        window.history.back();
    }
}
