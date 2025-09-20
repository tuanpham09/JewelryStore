import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatOptionModule } from '@angular/material/core';
import { AuthService } from '../auth.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';

interface ProductImage {
    id: string;
    url: string;
    alt: string;
}

interface ProductReview {
    id: string;
    userName: string;
    rating: number;
    comment: string;
    date: string;
    verified: boolean;
}

interface Product {
    id: string;
    name: string;
    price: number;
    originalPrice?: number;
    images: ProductImage[];
    category: string;
    description: string;
    detailedDescription: string;
    specifications: { [key: string]: string };
    reviews: ProductReview[];
    averageRating: number;
    totalReviews: number;
    inStock: boolean;
    stockQuantity: number;
    isFavorite: boolean;
    tags: string[];
}

interface RelatedProduct {
    id: string;
    name: string;
    price: number;
    image: string;
    category: string;
}

@Component({
    selector: 'app-product-detail',
    imports: [
        CommonModule,
        FormsModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatTabsModule,
        MatChipsModule,
        MatBadgeModule,
        MatDividerModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatOptionModule,
        Header,
        Footer
    ],
    templateUrl: './product-detail.html',
    styleUrl: './product-detail.css'
})
export class ProductDetail implements OnInit {
    currentUser: any = null;
    product: Product | null = null;
    selectedImageIndex: number = 0;
    quantity: number = 1;
    selectedSize: string = '';
    selectedColor: string = '';
    relatedProducts: RelatedProduct[] = [];
    newReview = {
        rating: 5,
        comment: ''
    };

    sizes = ['S', 'M', 'L'];
    colors = ['Vàng', 'Bạc', 'Vàng hồng'];

    // Expose Math to template
    Math = Math;

    constructor(
        private route: ActivatedRoute,
        public router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
        });

        this.route.params.subscribe(params => {
            const productId = params['id'];
            this.loadProduct(productId);
        });
    }

    loadProduct(productId: string) {
        // Mock data - trong thực tế sẽ gọi API
        this.product = {
            id: productId,
            name: 'Vòng tay ngọc trai Corrine - vàng mờ',
            price: 990000,
            originalPrice: 1200000,
            images: [
                {
                    id: '1',
                    url: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=600&h=600&fit=crop&crop=center',
                    alt: 'Vòng tay ngọc trai Corrine - vàng mờ'
                },
                {
                    id: '2',
                    url: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=600&h=600&fit=crop&crop=center',
                    alt: 'Vòng tay ngọc trai Corrine - góc nhìn khác'
                },
                {
                    id: '3',
                    url: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600&h=600&fit=crop&crop=center',
                    alt: 'Vòng tay ngọc trai Corrine - chi tiết'
                },
                {
                    id: '4',
                    url: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=600&h=600&fit=crop&crop=center',
                    alt: 'Vòng tay ngọc trai Corrine - trên tay'
                }
            ],
            category: 'bracelets',
            description: 'Vòng tay thanh lịch với ngọc trai tự nhiên và vàng mờ sang trọng, phù hợp cho mọi dịp',
            detailedDescription: `
        <p>Vòng tay ngọc trai Corrine là một tác phẩm nghệ thuật tinh tế, được chế tác từ những viên ngọc trai tự nhiên cao cấp nhất. Mỗi viên ngọc trai được lựa chọn kỹ lưỡng với độ bóng hoàn hảo và kích thước đồng đều.</p>
        
        <p>Chất liệu vàng mờ 18K tạo nên vẻ sang trọng và tinh tế, không quá lộng lẫy nhưng vẫn toát lên sự quý phái. Thiết kế đơn giản nhưng thanh lịch, phù hợp với mọi phong cách thời trang từ casual đến formal.</p>
        
        <p>Sản phẩm được chế tác thủ công bởi những nghệ nhân có kinh nghiệm lâu năm, đảm bảo chất lượng và độ bền cao. Vòng tay có thể điều chỉnh kích thước phù hợp với cổ tay của bạn.</p>
      `,
            specifications: {
                'Chất liệu': 'Vàng mờ 18K, Ngọc trai tự nhiên',
                'Kích thước': 'Có thể điều chỉnh 16-20cm',
                'Trọng lượng': '8.5g',
                'Xuất xứ': 'Việt Nam',
                'Bảo hành': '12 tháng',
                'Chứng nhận': 'Giấy chứng nhận vàng 18K'
            },
            reviews: [
                {
                    id: '1',
                    userName: 'Nguyễn Thị Lan',
                    rating: 5,
                    comment: 'Vòng tay rất đẹp và sang trọng. Chất lượng ngọc trai rất tốt, bóng và đều. Tôi rất hài lòng với sản phẩm này.',
                    date: '2024-01-15',
                    verified: true
                },
                {
                    id: '2',
                    userName: 'Trần Văn Minh',
                    rating: 4,
                    comment: 'Sản phẩm đẹp, giao hàng nhanh. Chỉ có điều giá hơi cao nhưng chất lượng tương xứng.',
                    date: '2024-01-10',
                    verified: true
                },
                {
                    id: '3',
                    userName: 'Lê Thị Hoa',
                    rating: 5,
                    comment: 'Vòng tay rất tinh tế và thanh lịch. Phù hợp với mọi trang phục. Tôi sẽ mua thêm sản phẩm khác.',
                    date: '2024-01-08',
                    verified: false
                }
            ],
            averageRating: 4.7,
            totalReviews: 3,
            inStock: true,
            stockQuantity: 15,
            isFavorite: false,
            tags: ['Ngọc trai', 'Vàng mờ', 'Thanh lịch', 'Cao cấp']
        };

        this.loadRelatedProducts();
    }

    loadRelatedProducts() {
        this.relatedProducts = [
            {
                id: '2',
                name: 'Nhẫn đôi liên kết ngọc trai Corrine - vàng hồng',
                price: 550000,
                image: 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=300&h=300&fit=crop&crop=center',
                category: 'rings'
            },
            {
                id: '4',
                name: 'Bông tai hoa pha lê Felicity - vàng hồng',
                price: 1050000,
                image: 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=300&h=300&fit=crop&crop=center',
                category: 'earrings'
            },
            {
                id: '9',
                name: 'Vòng tay charm hoa hồng - vàng hồng',
                price: 750000,
                image: 'https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=300&h=300&fit=crop&crop=center',
                category: 'bracelets'
            },
            {
                id: '10',
                name: 'Dây chuyền trái tim - vàng hồng',
                price: 850000,
                image: 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=300&h=300&fit=crop&crop=center',
                category: 'necklaces'
            }
        ];
    }

    selectImage(index: number) {
        this.selectedImageIndex = index;
    }

    increaseQuantity() {
        if (this.product && this.quantity < this.product.stockQuantity) {
            this.quantity++;
        }
    }

    decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    toggleFavorite() {
        if (this.product) {
            this.product.isFavorite = !this.product.isFavorite;
        }
    }

    addToCart() {
        if (this.product) {
            // Logic thêm vào giỏ hàng
            console.log('Added to cart:', {
                product: this.product,
                quantity: this.quantity,
                size: this.selectedSize,
                color: this.selectedColor
            });
        }
    }

    buyNow() {
        if (this.product) {
            // Logic mua ngay
            console.log('Buy now:', {
                product: this.product,
                quantity: this.quantity,
                size: this.selectedSize,
                color: this.selectedColor
            });
        }
    }

    submitReview() {
        if (this.newReview.comment.trim()) {
            const review: ProductReview = {
                id: Date.now().toString(),
                userName: this.currentUser?.fullName || 'Khách hàng',
                rating: this.newReview.rating,
                comment: this.newReview.comment,
                date: new Date().toISOString().split('T')[0],
                verified: false
            };

            if (this.product) {
                this.product.reviews.unshift(review);
                this.product.totalReviews++;
                this.calculateAverageRating();
            }

            this.newReview = { rating: 5, comment: '' };
        }
    }

    calculateAverageRating() {
        if (this.product) {
            const totalRating = this.product.reviews.reduce((sum, review) => sum + review.rating, 0);
            this.product.averageRating = totalRating / this.product.reviews.length;
        }
    }

    goBack() {
        this.router.navigate(['/']);
    }

    navigateToProduct(productId: string) {
        this.router.navigate(['/product', productId]);
    }

    navigateToCart() {
        this.router.navigate(['/cart']);
    }
}
