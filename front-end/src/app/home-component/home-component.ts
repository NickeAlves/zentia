import { Component } from '@angular/core';
import { Navbar } from '../components/navbar/navbar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-component',
  imports: [Navbar],
  templateUrl: './home-component.html',
  styleUrl: './home-component.css',
})
export class HomeComponent {
  constructor(private router: Router) {}

  navigateToDashboard(): void {
    this.router.navigate(['dashboard']);
  }
}
