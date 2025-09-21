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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../auth.service';
import { ProductService, Product, ProductSize, ProductImage, ProductReview } from '../services/product.service';
import { CartService, CartItem } from '../services/cart.service';
import { Header } from '../shared/header/header';
import { Footer } from '../shared/footer/footer';


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
        MatProgressSpinnerModule,
        MatSnackBarModule,
        Header,
        Footer
    ],
    templateUrl: 'product-detail.html',
    styleUrl: 'product-detail.css'
})
export class ProductDetail implements OnInit {
    currentUser: any = null;
    product: Product | null = null;
    selectedImageIndex: number = 0;
    quantity: number = 1;
    selectedSize: string = '';
    selectedColor: string = '';
    relatedProducts: Product[] = [];
    newReview = {
        rating: 5,
        comment: ''
    };
    loading = false;
    error: string | null = null;
    hoverRating = 0;
    sortBy = 'newest';
    cartItemCount = 0;

    // Expose Math to template
    Math = Math;

    constructor(
        public route: ActivatedRoute,
        public router: Router,
        private authService: AuthService,
        private productService: ProductService,
        private cartService: CartService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit() {
        this.authService.currentUser$.subscribe(user => {
            this.currentUser = user;
            
            // Xử lý pending cart item sau khi đăng nhập
            if (user) {
                this.handlePendingCartItem();
            }
        });

        // Subscribe to cart changes
        this.cartService.cartItems$.subscribe(cartItems => {
            this.cartItemCount = this.cartService.getCartItemCount();
        });

        this.route.params.subscribe(params => {
            const productId = params['id'];
            this.loadProduct(productId);
        });
    }

    private handlePendingCartItem() {
        const pendingCartItemStr = localStorage.getItem('pending_cart_item');
        if (pendingCartItemStr) {
            try {
                const pendingCartItem = JSON.parse(pendingCartItemStr);
                
                // Kiểm tra xem pending item có phải cho sản phẩm hiện tại không
                if (pendingCartItem.productId === this.product?.id) {
                    const cartItem: Omit<CartItem, 'id' | 'addedAt'> = {
                        productId: pendingCartItem.productId,
                        productName: pendingCartItem.productName,
                        productImage: pendingCartItem.productImage,
                        unitPrice: pendingCartItem.price,
                        quantity: pendingCartItem.quantity,
                        subtotal: pendingCartItem.price * pendingCartItem.quantity,
                        sizeValue: pendingCartItem.size,
                        colorValue: pendingCartItem.color
                    };

                    this.cartService.addToCart(cartItem).subscribe(success => {
                        if (success) {
                            // Xóa pending item
                            localStorage.removeItem('pending_cart_item');
                            
                            if (pendingCartItem.checkout) {
                                // Chuyển đến checkout nếu có flag checkout
                                this.router.navigate(['/checkout']);
                            } else {
                                // Hiển thị thông báo thành công
                                this.snackBar.open('Đã thêm vào giỏ hàng', 'Đóng', {
                                    duration: 3000
                                });
                            }
                        }
                    });
                }
            } catch (error) {
                console.error('Error handling pending cart item:', error);
                localStorage.removeItem('pending_cart_item');
            }
        }
    }

    loadProduct(productId: string) {
        this.loading = true;
        this.error = null;

        this.productService.getProductById(+productId).subscribe({
            next: (response) => {
                if (response.success && response.data) {
                    this.product = Array.isArray(response.data) ? response.data[0] : response.data;
        this.loadRelatedProducts();
                    this.incrementViewCount();
                } else {
                    this.error = 'Không thể tải thông tin sản phẩm';
                }
                this.loading = false;
            },
            error: (error) => {
                console.error('Error loading product:', error);
                this.error = 'Có lỗi xảy ra khi tải sản phẩm';
                this.loading = false;
            }
        });
    }

    loadRelatedProducts() {
        if (!this.product) return;

        this.productService.getRelatedProducts(this.product.id, 4).subscribe({
            next: (response) => {
                if (response.success && response.data) {
                    this.relatedProducts = Array.isArray(response.data) ? response.data : [response.data];
                }
            },
            error: (error) => {
                console.error('Error loading related products:', error);
            }
        });
    }

    incrementViewCount() {
        if (!this.product) return;

        this.productService.incrementViewCount(this.product.id).subscribe({
            next: (response) => {
                if (response.success && response.data) {
                    this.product = Array.isArray(response.data) ? response.data[0] : response.data;
                }
            },
            error: (error) => {
                console.error('Error incrementing view count:', error);
            }
        });
    }

    selectImage(index: number) {
        this.selectedImageIndex = index;
    }

    increaseQuantity() {
        if (this.product && this.quantity < this.getMaxQuantity()) {
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
        if (!this.product || !this.isInStock()) {
            this.snackBar.open('Sản phẩm không có sẵn', 'Đóng', {
                duration: 3000
            });
            return;
        }

        // Kiểm tra user và token
        const currentUser = this.authService.getCurrentUser();
        const token = localStorage.getItem('token');


        if (currentUser && token) {
            // User đã đăng nhập - lưu vào giỏ hàng của user
            this.addToUserCart();
        } else {
            // User chưa đăng nhập - yêu cầu đăng nhập
            this.requireLoginForCart();
        }
    }

    private addToUserCart() {
        if (!this.product) return;

        const cartItem: Omit<CartItem, 'id' | 'addedAt'> = {
            productId: this.product.id,
            productName: this.product.name,
            productImage: this.getPrimaryImage(),
            unitPrice: this.getCurrentPrice(),
            quantity: this.quantity,
            subtotal: this.getCurrentPrice() * this.quantity,
            sizeValue: this.selectedSize || undefined,
            colorValue: this.selectedColor || undefined
        };

        console.log('ProductDetail - Adding to cart:', {
            productId: cartItem.productId,
            sizeValue: cartItem.sizeValue,
            colorValue: cartItem.colorValue,
            quantity: cartItem.quantity
        });

        this.cartService.addToCart(cartItem).subscribe(success => {
            if (success) {
                this.snackBar.open('Đã thêm vào giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            } else {
                this.snackBar.open('Có lỗi xảy ra khi thêm vào giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    private requireLoginForCart() {

        // Hiển thị dialog yêu cầu đăng nhập
        this.snackBar.open('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng', 'Đăng nhập', {
            duration: 5000
        }).onAction().subscribe(() => {
            // Lưu URL hiện tại để callback sau khi đăng nhập
            localStorage.setItem('callback_url', this.router.url);
            this.router.navigate(['/login']);
        });
    }

    buyNow() {
        if (!this.product || !this.isInStock()) {
            this.snackBar.open('Sản phẩm không có sẵn', 'Đóng', {
                duration: 3000
            });
            return;
        }

        if (this.currentUser) {
            // User đã đăng nhập - thêm vào giỏ hàng và chuyển đến checkout
            this.addToUserCartAndCheckout();
        } else {
            // User chưa đăng nhập - yêu cầu đăng nhập
            this.requireLoginForCheckout();
        }
    }

    private addToUserCartAndCheckout() {
        if (!this.product) return;

        const cartItem: Omit<CartItem, 'id' | 'addedAt'> = {
            productId: this.product.id,
            productName: this.product.name,
            productImage: this.getPrimaryImage(),
            unitPrice: this.getCurrentPrice(),
                quantity: this.quantity,
            subtotal: this.getCurrentPrice() * this.quantity,
            sizeValue: this.selectedSize || undefined,
            colorValue: this.selectedColor || undefined
        };

        this.cartService.addToCart(cartItem).subscribe(success => {
            if (success) {
                this.router.navigate(['/checkout']);
            } else {
                this.snackBar.open('Có lỗi xảy ra khi thêm vào giỏ hàng', 'Đóng', {
                    duration: 3000
                });
            }
        });
    }

    private requireLoginForCheckout() {
        // Lưu thông tin sản phẩm và checkout flag
        const pendingCartItem = {
            productId: this.product!.id,
            productName: this.product!.name,
            productImage: this.getPrimaryImage(),
            price: this.getCurrentPrice(),
            quantity: this.quantity,
            size: this.selectedSize || undefined,
            color: this.selectedColor || undefined,
            timestamp: Date.now(),
            checkout: true // Flag để biết cần chuyển đến checkout sau khi đăng nhập
        };

        localStorage.setItem('pending_cart_item', JSON.stringify(pendingCartItem));

        // Hiển thị dialog yêu cầu đăng nhập
        this.snackBar.open('Vui lòng đăng nhập để mua hàng', 'Đăng nhập', {
            duration: 5000
        }).onAction().subscribe(() => {
            // Lưu URL hiện tại để callback sau khi đăng nhập
            localStorage.setItem('callback_url', this.router.url);
            this.router.navigate(['/login']);
        });
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
                if (!this.product.reviews) {
                    this.product.reviews = [];
                }
                this.product.reviews.unshift(review);
                this.product.totalReviews = (this.product.totalReviews || 0) + 1;
                this.calculateAverageRating();
            }

            this.newReview = { rating: 5, comment: '' };
        }
    }

    calculateAverageRating() {
        if (this.product && this.product.reviews) {
            const totalRating = this.product.reviews.reduce((sum: number, review: ProductReview) => sum + review.rating, 0);
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

    // Helper methods
    getAvailableSizes(): string[] {
        if (!this.product || !this.product.sizes) return [];
        return this.product.sizes
            .filter(size => size.isActive && size.stock > 0)
            .map(size => size.size);
    }

    getStockForSize(size: string): number {
        if (!this.product || !this.product.sizes) return 0;
        const productSize = this.product.sizes.find(s => s.size === size && s.isActive);
        return productSize ? productSize.stock : 0;
    }

    getMaxQuantity(): number {
        if (!this.product) return 0;
        if (this.selectedSize) {
            return this.getStockForSize(this.selectedSize);
        }
        return this.product.stock;
    }

    getProductImages(): ProductImage[] {
        if (!this.product || !this.product.images) {
            // Fallback to thumbnail if no images
            return [{
                id: 0,
                imageUrl: this.product?.thumbnail || '',
                altText: this.product?.name || '',
                isPrimary: true,
                sortOrder: 0
            }];
        }
        return this.product.images.sort((a, b) => a.sortOrder - b.sortOrder);
    }

    getPrimaryImage(): string {
        const images = this.getProductImages();
        const primaryImage = images.find(img => img.isPrimary);
        return primaryImage ? primaryImage.imageUrl : (images[0]?.imageUrl || '');
    }

    getCurrentPrice(): number {
        if (!this.product) return 0;
        return this.product.currentPrice || this.product.salePrice || this.product.price;
    }

    getDiscountPercentage(): number {
        if (!this.product || !this.product.originalPrice) return 0;
        const currentPrice = this.getCurrentPrice();
        return Math.round((1 - currentPrice / this.product.originalPrice) * 100);
    }

    isInStock(): boolean {
        if (!this.product) return false;
        if (this.selectedSize) {
            return this.getStockForSize(this.selectedSize) > 0;
        }
        return this.product.stock > 0;
    }

    getStockQuantity(): number {
        if (!this.product) return 0;
        if (this.selectedSize) {
            return this.getStockForSize(this.selectedSize);
        }
        return this.product.stock;
    }

    // Review methods
    getRatingText(rating: number): string {
        const texts = {
            1: 'Rất không hài lòng',
            2: 'Không hài lòng',
            3: 'Bình thường',
            4: 'Hài lòng',
            5: 'Rất hài lòng'
        };
        return texts[rating as keyof typeof texts] || '';
    }

    getRatingCount(rating: number): number {
        if (!this.product || !this.product.reviews) return 0;
        return this.product.reviews.filter((review: ProductReview) => review.rating === rating).length;
    }

    getRatingPercentage(rating: number): number {
        if (!this.product || !this.product.reviews || this.product.reviews.length === 0) return 0;
        const count = this.getRatingCount(rating);
        return (count / this.product.reviews.length) * 100;
    }

    getSortedReviews(): ProductReview[] {
        if (!this.product || !this.product.reviews) return [];
        
        const reviews = [...this.product.reviews];
        
        switch (this.sortBy) {
            case 'newest':
                return reviews.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
            case 'oldest':
                return reviews.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
            case 'highest':
                return reviews.sort((a, b) => b.rating - a.rating);
            case 'lowest':
                return reviews.sort((a, b) => a.rating - b.rating);
            default:
                return reviews;
        }
    }

    formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('vi-VN', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    resetReviewForm() {
        this.newReview = {
            rating: 5,
            comment: ''
        };
        this.hoverRating = 0;
    }

    likeReview(reviewId: string) {
        // TODO: Implement like functionality
        console.log('Like review:', reviewId);
        this.snackBar.open('Cảm ơn bạn đã đánh giá hữu ích!', 'Đóng', {
            duration: 3000
        });
    }

    reportReview(reviewId: string) {
        // TODO: Implement report functionality
        console.log('Report review:', reviewId);
        this.snackBar.open('Cảm ơn bạn đã báo cáo. Chúng tôi sẽ xem xét đánh giá này.', 'Đóng', {
            duration: 3000
        });
    }
}
