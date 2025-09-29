import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

import { ProductService, Product, SearchResponse } from '../services/product.service';

// Extended Product interface for Deal Of The Day
interface DealProduct extends Product {
  dealEnded?: boolean;
  brand?: string;
}

@Component({
  selector: 'app-home-maia',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    Header,
    Footer
  ],
  templateUrl: './home-maia.html',
  styleUrl: './home-maia.css'
})
export class HomeMaia implements OnInit {
  isLoadingNew: boolean = false;
  isLoadingOnSale: boolean = false;
  isLoadingDeal: boolean = false;
  newProducts: Product[] = [];
  onSaleProducts: Product[] = [];
  dealProducts: DealProduct[] = [];
  
  // Countdown timer
  countdown = {
    days: 0,
    hours: 0,
    minutes: 0,
    seconds: 0
  };

  // Banner slider properties
  currentSlide: number = 0;
  banners: any[] = [
    {
      title: 'Diamonds Jewellery Collection',
      description: 'Give a contemporary spin on classic pearls this holiday. What dreams are made of.',
      buttonText: 'LEARN MORE',
      image: 'assets/banner1.jpg'
    },
    {
      title: 'Paloma Picasso Necklace in Silver',
      description: 'Paloma Picasso\'s creations artfully combine timeless sophistication with bold style.',
      buttonText: 'LEARN MORE',
      image: 'assets/banner2.jpg'
    },
    {
      title: 'Luxury Gold Collection',
      description: 'Discover our exclusive collection of handcrafted gold jewelry pieces.',
      buttonText: 'LEARN MORE',
      image: 'assets/banner3.jpg'
    }
  ];

  constructor(
    private productService: ProductService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadNewProducts();
    this.loadOnSaleProducts();
    this.loadDealProducts();
    this.startCountdown();
  }

  loadNewProducts() {
    this.isLoadingNew = true;
    this.productService.getNewProducts(0, 8).subscribe({
      next: (response: SearchResponse) => {
        if (response.success) {
          this.newProducts = response.data.content;
        }
        this.isLoadingNew = false;
      },
      error: () => {
        this.isLoadingNew = false;
      }
    });
  }

  loadOnSaleProducts() {
    this.isLoadingOnSale = true;
    this.productService.getOnSaleProducts(0, 8).subscribe({
      next: (response: SearchResponse) => {
        if (response.success) {
          this.onSaleProducts = response.data.content;
        }
        this.isLoadingOnSale = false;
      },
      error: () => {
        this.isLoadingOnSale = false;
      }
    });
  }

  navigateToProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }

  scrollToProducts() {
    const el = document.getElementById('products');
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  }

  addToCart(product: Product) {
    this.snackBar.open('Vui lòng thêm vào giỏ từ trang chi tiết sản phẩm', 'Đóng', { duration: 2500 });
  }

  // Banner slider methods
  nextSlide() {
    if (this.currentSlide < this.banners.length - 1) {
      this.currentSlide++;
    }
  }

  previousSlide() {
    if (this.currentSlide > 0) {
      this.currentSlide--;
    }
  }

  goToSlide(index: number) {
    if (index >= 0 && index < this.banners.length) {
      this.currentSlide = index;
    }
  }

  // Deal Of The Day methods
  loadDealProducts() {
    this.isLoadingDeal = true;
    this.productService.getOnSaleProducts(0, 4).subscribe({
      next: (response: SearchResponse) => {
        if (response.success) {
          this.dealProducts = response.data.content.map(product => ({
            ...product,
            dealEnded: Math.random() > 0.5, // Random deal ended status for demo
            brand: this.getRandomBrand()
          } as DealProduct));
        }
        this.isLoadingDeal = false;
      },
      error: () => {
        this.isLoadingDeal = false;
      }
    });
  }

  getRandomBrand(): string {
    const brands = ['Elsa Peretti®', 'Paloma Picasso®', 'Atlas®', 'Tiffany & Co.', 'Cartier'];
    return brands[Math.floor(Math.random() * brands.length)];
  }

  startCountdown() {
    // Set countdown to 24 hours from now
    const endTime = new Date().getTime() + (24 * 60 * 60 * 1000);
    
    const updateCountdown = () => {
      const now = new Date().getTime();
      const distance = endTime - now;

      if (distance < 0) {
        this.countdown = { days: 0, hours: 0, minutes: 0, seconds: 0 };
        return;
      }

      this.countdown.days = Math.floor(distance / (1000 * 60 * 60 * 24));
      this.countdown.hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      this.countdown.minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
      this.countdown.seconds = Math.floor((distance % (1000 * 60)) / 1000);
    };

    updateCountdown();
    setInterval(updateCountdown, 1000);
  }

  viewAllDeals() {
    this.router.navigate(['/search'], { 
      queryParams: { 
        category: 'deal',
        sort: 'price',
        order: 'asc'
      } 
    });
  }
}



