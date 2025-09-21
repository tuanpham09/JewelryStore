import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Kiểm tra xem có phải đang chạy trong browser không
  if (typeof window !== 'undefined' && window.localStorage) {
    const token = localStorage.getItem('token');
    
    if (token) {
      const cloned = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`)
      });
      return next(cloned);
    }
  }
  
  return next(req);
};