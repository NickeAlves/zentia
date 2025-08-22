import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthBlockerGuard implements CanActivate {
  allowedRoutes: string[] = ['/', '/auth/register', '/auth/login'];

  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    const token = localStorage.getItem('auth_token');

    if (token) {
      return true;
    }

    return this.router.createUrlTree(['/auth/login']);
  }
}
