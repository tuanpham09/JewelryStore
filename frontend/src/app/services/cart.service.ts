import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface CartItem {
  id: string;
  productId: number;
  productName: string;
  productImage: string;
  price: number;
  quantity: number;
  size?: string;
  color?: string;
  addedAt: Date;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private cartItemsSubject = new BehaviorSubject<CartItem[]>([]);
  public cartItems$ = this.cartItemsSubject.asObservable();
  
  private readonly CART_STORAGE_KEY = 'jewelry_cart';

  constructor() {
    this.loadCartFromStorage();
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
  addToCart(item: Omit<CartItem, 'id' | 'addedAt'>): void {
    const cartItems = this.cartItemsSubject.value;
    const existingItemIndex = cartItems.findIndex(
      cartItem => 
        cartItem.productId === item.productId && 
        cartItem.size === item.size && 
        cartItem.color === item.color
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
  removeFromCart(itemId: string): void {
    const cartItems = this.cartItemsSubject.value.filter(item => item.id !== itemId);
    this.cartItemsSubject.next(cartItems);
    this.saveCartToStorage(cartItems);
  }

  // Update item quantity
  updateQuantity(itemId: string, quantity: number): void {
    if (quantity <= 0) {
      this.removeFromCart(itemId);
      return;
    }

    const cartItems = this.cartItemsSubject.value.map(item => 
      item.id === itemId ? { ...item, quantity } : item
    );
    this.cartItemsSubject.next(cartItems);
    this.saveCartToStorage(cartItems);
  }

  // Clear cart
  clearCart(): void {
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
    return this.cartItemsSubject.value.reduce((total, item) => total + (item.price * item.quantity), 0);
  }

  // Check if product is in cart
  isInCart(productId: number, size?: string, color?: string): boolean {
    return this.cartItemsSubject.value.some(
      item => 
        item.productId === productId && 
        item.size === size && 
        item.color === color
    );
  }

  // Get quantity of specific product in cart
  getProductQuantity(productId: number, size?: string, color?: string): number {
    const item = this.cartItemsSubject.value.find(
      cartItem => 
        cartItem.productId === productId && 
        cartItem.size === size && 
        cartItem.color === color
    );
    return item ? item.quantity : 0;
  }

  // Generate unique ID
  private generateId(): string {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
  }

  // Sync cart with server (for logged in users)
  syncCartWithServer(): Observable<any> {
    // TODO: Implement API call to sync cart with server
    // This will be called when user logs in
    return new Observable(observer => {
      // For now, just return success
      observer.next({ success: true });
      observer.complete();
    });
  }
}
