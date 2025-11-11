import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const auth: AuthService = inject(AuthService);
  const token = auth.token();

  if (token == null) {
    return next(request);
  } else {
    return next(
      request.clone({
        headers: request.headers.set('Authorization', `Bearer ${token}`),
      })
    );
  }
};
