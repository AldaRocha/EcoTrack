import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { NavbarComponent } from "./navbar/navbar.component";
import { SidebarComponent } from "./sidebar/sidebar.component";
import { RouterOutlet } from "@angular/router";

@Component({
    selector: "app-menu",
    templateUrl: "./menu.component.html",
    styleUrls: ["./menu.component.css"],
    standalone: true,
    imports: [
        CommonModule,
        SidebarComponent,
        NavbarComponent,
        RouterOutlet
    ]
})
export class MenuComponent{

}