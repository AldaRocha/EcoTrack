import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { Header } from "../header/header";
import { Footer } from "../footer/footer";

@Component({
    selector: "app-home",
    templateUrl: "./home.component.html",
    styleUrls: ["./home.component.css"],
    standalone: true,
    imports: [
        CommonModule,
        Header,
        Footer,
    ]
    
})
export class HomeComponent{
    
}
