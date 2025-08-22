import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home-component/home-component';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { DashboardComponent } from './dashboard-component/dashboard-component';
import { AuthBlockerGuard } from './auth/auth-blocker-guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthBlockerGuard] },
  { path: 'auth/login', component: Login },
  { path: 'auth/register', component: Register },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
