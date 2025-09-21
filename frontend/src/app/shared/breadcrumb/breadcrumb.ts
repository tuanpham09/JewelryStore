import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

export interface BreadcrumbItem {
    label: string;
    url?: string;
    active?: boolean;
}

@Component({
    selector: 'app-breadcrumb',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule
    ],
    templateUrl: './breadcrumb.html',
    styleUrl: './breadcrumb.css'
})
export class BreadcrumbComponent {
    @Input() items: BreadcrumbItem[] = [];

    constructor(private router: Router) { }

    navigateTo(item: BreadcrumbItem) {
        if (item.url && !item.active) {
            this.router.navigate([item.url]);
        }
    }
}
