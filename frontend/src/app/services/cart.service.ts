import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of, switchMap, catchError } from 'rxjs';
import { CartApiService, ApiCartItem } from './cart-api.service';
import { AuthService } from '../auth.service';

export interface CartItem {
  id: string;
  productId: number;
  productName: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  subtotal: number;
  sizeValue?: string;
  colorValue?: string;
  addedAt: Date;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this.cartItemsSubject.asObservable();
  
  private readonly CART_STORAGE_KEY = 'jewelry_cart';

  constructor(
    private cartApiService: CartApiService,
    private authService: AuthService
  ) {
    // Load cart based on current auth status
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      this.loadCartFromServer().subscribe();
    } else {
      this.loadCartFromStorage();
    }
    
    // Subscribe to auth changes to sync cart
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.syncCartWithServer();
      } else {
        this.loadCartFromStorage();
      }
    });
  }

  // Load cart from localStorage
  private loadCartFromStorage(): void {
    try {
      const storedCart = localStorage.getItem(this.CART_STORAGE_KEY);
      if (storedCart) {
        const cartItems = JSON.parse(storedCart).map((item: any) => ({
          ...item,
          addedAt: new Date(item.addedAt)
        }));
        this.cartItemsSubject.next(cartItems);
      }
    } catch (error) {
      console.error('Error loading cart from storage:', error);
      this.cartItemsSubject.next([]);
    }
  }

  // Save cart to localStorage
  private saveCartToStorage(cartItems: CartItem[]): void {
    try {
      localStorage.setItem(this.CART_STORAGE_KEY, JSON.stringify(cartItems));
    } catch (error) {
      console.error('Error saving cart to storage:', error);
    }
  }

  // Add item to cart
  addToCart(item: Omit<CartItem, 'id' | 'addedAt'>): Observable<boolean> {
    const currentUser = this.authService.getCurrentUser();
    const token = localStorage.getItem('token');
    
    if (currentUser && token) {
      // User is logged in - save to database
      return this.addToServerCart(item);
    } else {
      // User not logged in - save to localStorage
      this.addToLocalCart(item);
      return of(true);
    }
  }

  private addToServerCart(item: Omit<CartItem, 'id' | 'addedAt'>): Observable<boolean> {
    const request = {
      productId: item.productId,
      quantity: item.quantity,
      sizeValue: item.sizeValue,
      colorValue: item.colorValue
    };

    console.log('CartService - Adding to server cart:', request);
    console.log('CartService - Current user:', this.authService.getCurrentUser());
    console.log('CartService - Token:', localStorage.getItem('token'));

    return this.cartApiService.addToCart(request).pipe(
      switchMap(() => this.loadCartFromServer()),
      catchError(error => {
        console.error('Error adding to server cart:', error);
        console.error('Error details:', error.error);
        // Fallback to local storage
        this.addToLocalCart(item);
        return of(false);
      })
    );
  }

  private addToLocalCart(item: Omit<CartItem, 'id' | 'addedAt'>): void {
    const cartItems = this.cartItemsSubject.value;
    const existingItemIndex = cartItems.findIndex(
      cartItem => 
        cartItem.productId === item.productId && 
        cartItem.sizeValue === item.sizeValue && 
        cartItem.colorValue === item.colorValue
    );

    if (existingItemIndex > -1) {
      // Update existing item quantity
      cartItems[existingItemIndex].quantity += item.quantity;
    } else {
      // Add new item
      const newItem: CartItem = {
        ...item,
        id: this.generateId(),
        addedAt: new Date()
      };
      cartItems.push(newItem);
    }

    this.cartItemsSubject.next(cartItems);
    this.saveCartToStorage(cartItems);
  }

  // Remove item from cart
  removeFromCart(itemId: string): Observable<boolean> {
    const currentUser = this.authService.getCurrentUser();
    
    if (currentUser) {
      // User is logged in - remove from database
      return this.removeFromServerCart(itemId);
    } else {
      // User not logged in - remove from localStorage
      this.removeFromLocalCart(itemId);
      return of(true);
    }
  }

  private removeFromServerCart(itemId: string): Observable<boolean> {
    // Tìm item trong cart để lấy productId, sizeValue, colorValue
    const cartItems = this.cartItemsSubject.value;
    const item = cartItems.find(cartItem => cartItem.id === itemId);
    
    if (!item) {
      console.error('Item not found in cart:', itemId);
      return of(false);
    }
    
    return this.cartApiService.removeFromCart(item.productId, item.sizeValue, item.colorValue).pipe(
      switchMap(() => this.loadCartFromServer()),
      catchError(error => {
        console.error('Error removing from server cart:', error);
        return of(false);
      })
    );
  }

  private removeFromLocalCart(itemId: string): void {
    const cartItems = this.cartItemsSubject.value.filter(item => item.id !== itemId);
    this.cartItemsSubject.next(cartItems);
    this.saveCartToStorage(cartItems);
  }

  // Update item quantity
  updateQuantity(itemId: string, quantity: number): Observable<boolean> {
    if (quantity <= 0) {
      return this.removeFromCart(itemId);
    }

    const currentUser = this.authService.getCurrentUser();
    
    if (currentUser) {
      // User is logged in - update in database
      return this.updateServerCartQuantity(itemId, quantity);
    } else {
      // User not logged in - update in localStorage
      this.updateLocalCartQuantity(itemId, quantity);
      return of(true);
    }
  }

  private updateServerCartQuantity(itemId: string, quantity: number): Observable<boolean> {
    // Tìm item trong cart để lấy productId, sizeValue, colorValue
    const cartItems = this.cartItemsSubject.value;
    const item = cartItems.find(cartItem => cartItem.id === itemId);
    
    if (!item) {
      console.error('Item not found in cart:', itemId);
      return of(false);
    }
    
    return this.cartApiService.updateCartItem(item.productId, { quantity }, item.sizeValue, item.colorValue).pipe(
      switchMap(() => this.loadCartFromServer()),
      catchError(error => {
        console.error('Error updating server cart:', error);
        return of(false);
      })
    );
  }

  private updateLocalCartQuantity(itemId: string, quantity: number): void {
    const cartItems = this.cartItemsSubject.value.map(item => 
      item.id === itemId ? { ...item, quantity } : item
    );
    this.cartItemsSubject.next(cartItems);
    this.saveCartToStorage(cartItems);
  }

  // Clear cart
  clearCart(): Observable<boolean> {
    const currentUser = this.authService.getCurrentUser();
    
    if (currentUser) {
      // User is logged in - clear from database
      return this.clearServerCart();
    } else {
      // User not logged in - clear from localStorage
      this.clearLocalCart();
      return of(true);
    }
  }

  private clearServerCart(): Observable<boolean> {
    return this.cartApiService.clearCart().pipe(
      switchMap(() => this.loadCartFromServer()),
      catchError(error => {
        console.error('Error clearing server cart:', error);
        return of(false);
      })
    );
  }

  private clearLocalCart(): void {
    this.cartItemsSubject.next([]);
    localStorage.removeItem(this.CART_STORAGE_KEY);
  }

  // Get cart items
  getCartItems(): CartItem[] {
    return this.cartItemsSubject.value;
  }

  // Get cart item count
  getCartItemCount(): number {
    return this.cartItemsSubject.value.reduce((total, item) => total + item.quantity, 0);
  }

  // Get cart total
  getCartTotal(): number {
    return this.cartItemsSubject.value.reduce((total, item) => total + item.subtotal, 0);
  }

  // Check if product is in cart
  isInCart(productId: number, size?: string, color?: string): boolean {
    return this.cartItemsSubject.value.some(
      item => 
        item.productId === productId && 
        item.sizeValue === size && 
        item.colorValue === color
    );
  }

  // Get quantity of specific product in cart
  getProductQuantity(productId: number, size?: string, color?: string): number {
    const item = this.cartItemsSubject.value.find(
      cartItem => 
        cartItem.productId === productId && 
        cartItem.sizeValue === size && 
        cartItem.colorValue === color
    );
    return item ? item.quantity : 0;
  }

  // Generate unique ID
  private generateId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }

  // Load cart from server (for logged in users)
  loadCartFromServer(): Observable<boolean> {
    console.log('Loading cart from server...');
    return this.cartApiService.getCartItems().pipe(
      switchMap(response => {
        console.log('Cart API response:', response);
        if (response.success) {
          const cartItems: CartItem[] = response.data.items.map(apiItem => ({
            id: apiItem.id.toString(),
            productId: apiItem.productId,
            productName: apiItem.productName,
            productImage: apiItem.productImage,
            unitPrice: apiItem.unitPrice,
            quantity: apiItem.quantity,
            subtotal: apiItem.subtotal,
            sizeValue: apiItem.sizeValue,
            colorValue: apiItem.colorValue,
            addedAt: new Date(apiItem.createdAt)
          }));
          console.log('Mapped cart items:', cartItems);
          this.cartItemsSubject.next(cartItems);
          return of(true);
        }
        console.log('Cart API response not successful');
        return of(false);
      }),
      catchError(error => {
        console.error('Error loading cart from server:', error);
        return of(false);
      })
    );
  }

  // Public method to refresh cart data
  refreshCart(): Observable<boolean> {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      // User đã đăng nhập - đồng bộ với localStorage trước, sau đó load từ server
      return this.syncCartWithServer();
    } else {
      // User chưa đăng nhập - chỉ load từ localStorage
      this.loadCartFromStorage();
      return of(true);
    }
  }

  // Sync cart with server (for logged in users)
  syncCartWithServer(): Observable<boolean> {
    const localCartItems = this.cartItemsSubject.value;
    
    if (localCartItems.length === 0) {
      // No local items, just load from server
      return this.loadCartFromServer();
    }

    // Sync local cart to server
    return this.cartApiService.syncCartFromLocalStorage(localCartItems).pipe(
      switchMap(() => this.loadCartFromServer()),
      catchError(error => {
        console.error('Error syncing cart with server:', error);
        // If sync fails, just load from server
        return this.loadCartFromServer();
      })
    );
  }
}
