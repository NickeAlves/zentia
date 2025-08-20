import { Component } from '@angular/core';
import { Navbar } from '../components/navbar/navbar';

@Component({
  selector: 'app-home-component',
  imports: [Navbar],
  templateUrl: './home-component.html',
  styleUrl: './home-component.css',
})
export class HomeComponent {}
