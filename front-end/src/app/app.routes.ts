import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { HomeComponent } from './home-component/home-component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'auth/login', component: Login },
  { path: 'auth/register', component: Register },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
