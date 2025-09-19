import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface Category {
  id: string;
  name: string;
}

interface Product {
  id: string;
  name: string;
  price: number;
  image: string;
  category: string;
  description: string;
}

interface ViewOption {
  value: number;
  label: string;
}

@Component({
  selector: 'app-home',
  imports: [
    CommonModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatSelectModule,
    MatOptionModule,
    MatFormFieldModule,
    Header,
    Footer
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  currentUser: any = null;
  selectedCategory: string = 'all';
  sortBy: string = 'featured';
  viewMode: number = 4;
  totalProducts: number = 12;

  categories: Category[] = [
    { id: 'all', name: 'Tất cả' },
    { id: 'earrings', name: 'Bông tai' },
    { id: 'rings', name: 'Nhẫn' },
    { id: 'bracelets', name: 'Vòng tay' },
    { id: 'necklaces', name: 'Dây chuyền' }
  ];

  viewOptions: ViewOption[] = [
    { value: 3, label: '3' },
    { value: 4, label: '4' },
    { value: 6, label: '6' }
  ];

  products: Product[] = [
    {
      id: '1',
      name: 'Vòng tay ngọc trai Corrine - vàng mờ',
      price: 990000,
      image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop&crop=center',
      category: 'bracelets',
      description: 'Vòng tay thanh lịch với ngọc trai tự nhiên và vàng mờ sang trọng, phù hợp cho mọi dịp'
    },
    {
      id: '2',
      name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
      price: 550000,
      image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop&crop=center',
      category: 'rings',
      description: 'Bộ nhẫn đôi với thiết kế liên kết tinh tế và ngọc trai, tượng trưng cho tình yêu vĩnh cửu'
    },
    {
      id: '3',
      name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng mờ',
      price: 550000,
      image: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=400&fit=crop&crop=center',
      category: 'rings',
      description: 'Nhẫn đôi với thiết kế liên kết và ngọc trai, hoàn hảo cho cặp đôi yêu nhau'
    },
    {
      id: '4',
      name: 'Bông tai hoa pha lê Felicity - vàng hồng',
      price: 1050000,
      image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop&crop=center',
      category: 'earrings',
      description: 'Bông tai với thiết kế hoa tinh tế và pha lê Swarovski, tỏa sáng như những bông hoa'
    },
    {
      id: '5',
      name: 'Bông tai hoa pha lê Felicity - bạc',
      price: 1050000,
      image: 'https://images.unsplash.com/photo-1515562141207-7a88fb7ce338?w=400&h=400&fit=crop&crop=center',
      category: 'earrings',
      description: 'Bông tai bạc với thiết kế hoa và pha lê, tạo vẻ đẹp thanh lịch và quyến rũ'
    },
    {
      id: '6',
      name: 'Dây chuyền hoa pha lê Felicity - bạc',
      price: 1090000,
      image: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=400&fit=crop&crop=center',
      category: 'necklaces',
      description: 'Dây chuyền bạc với thiết kế hoa và pha lê, điểm nhấn hoàn hảo cho trang phục'
    },
    {
      id: '7',
      name: 'Nhẫn chuỗi liên kết Nyra - bạc',
      price: 490000,
      image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop&crop=center',
      category: 'rings',
      description: 'Nhẫn bạc với thiết kế chuỗi liên kết hiện đại, phù hợp cho phong cách cá tính'
    },
    {
      id: '8',
      name: 'Nhẫn chuỗi liên kết Nyra - vàng',
      price: 490000,
      image: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=400&fit=crop&crop=center',
      category: 'rings',
      description: 'Nhẫn vàng với thiết kế chuỗi liên kết, tạo vẻ sang trọng và quý phái'
    },
    {
      id: '9',
      name: 'Vòng tay charm hoa hồng - vàng hồng',
      price: 750000,
      image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=400&fit=crop&crop=center',
      category: 'bracelets',
      description: 'Vòng tay với charm hoa hồng tinh tế, tượng trưng cho tình yêu và sự lãng mạn'
    },
    {
      id: '10',
      name: 'Dây chuyền trái tim - vàng hồng',
      price: 850000,
      image: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=400&h=400&fit=crop&crop=center',
      category: 'necklaces',
      description: 'Dây chuyền với mặt dây trái tim vàng hồng, biểu tượng của tình yêu vĩnh cửu'
    },
    {
      id: '11',
      name: 'Bông tai giọt nước - bạc',
      price: 650000,
      image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=400&h=400&fit=crop&crop=center',
      category: 'earrings',
      description: 'Bông tai với thiết kế giọt nước tinh tế, tạo vẻ đẹp thanh thoát và quyến rũ'
    },
    {
      id: '12',
      name: 'Nhẫn cưới kim cương - vàng trắng',
      price: 2500000,
      image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=400&h=400&fit=crop&crop=center',
      category: 'rings',
      description: 'Nhẫn cưới với kim cương tự nhiên và vàng trắng, biểu tượng của tình yêu vĩnh cửu'
    }
  ];

  get filteredProducts(): Product[] {
    let filtered = this.products;

    if (this.selectedCategory !== 'all') {
      filtered = filtered.filter(product => product.category === this.selectedCategory);
    }

    // Sort products
    switch (this.sortBy) {
      case 'price-low':
        filtered = filtered.sort((a, b) => a.price - b.price);
        break;
      case 'price-high':
        filtered = filtered.sort((a, b) => b.price - a.price);
        break;
      case 'newest':
        // For demo, just reverse the array
        filtered = filtered.reverse();
        break;
      default:
        // Keep original order for 'featured'
        break;
    }

    return filtered;
  }

  constructor(
    private authService: AuthService,
    public router: Router
  ) { }

  ngOnInit() {
    this.authService.loadUserFromToken();

    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  selectCategory(categoryId: string) {
    this.selectedCategory = categoryId;
  }

  setViewMode(mode: number) {
    this.viewMode = mode;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateToProduct(productId: string) {
    this.router.navigate(['/product', productId]);
  }

  addToCart(product: Product) {
    // Logic thêm vào giỏ hàng
    console.log('Added to cart:', product);
  }

  navigateToCart() {
    this.router.navigate(['/cart']);
  }
}