import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";

@Component({
    selector: "app-sidebar",
    templateUrl: "./sidebar.component.html",
    styleUrls: ["./sidebar.component.css"],
    standalone: true,
    imports: [
        CommonModule
    ]
})
export class SidebarComponent{
    
}