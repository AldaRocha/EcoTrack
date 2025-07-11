import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

import { isDevMode } from '@angular/core';
import { NgxSpinnerModule } from 'ngx-spinner';

declare let $: any;

@Component({
  selector: 'app-root',
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.css",
  standalone: true,
  imports: [
    RouterOutlet,
    NgxSpinnerModule
  ]
})
export class AppComponent implements OnInit {
  constructor(public router: Router){

  }

  ngOnInit() {
    if(isDevMode()){
      console.log("Development mode")
    } else{
      console.log("Production mode")
    }
    window.addEventListener("beforeunload", (e) => {
      e.preventDefault();
    });
  }
}
