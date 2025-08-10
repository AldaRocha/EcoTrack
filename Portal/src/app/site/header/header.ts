import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AboutUsComponent } from "../aboutus/aboutus.component";
import { ContactUsComponent } from "../contactus/contactus.component";
import { QuotationComponent } from "../quotation/quotation.component";

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {
  constructor(public router: Router) { }

  navigateTo(path: string) {
    this.router.navigateByUrl('/ecodev' + path);
    window.scrollTo(0, 0);
  }
}
