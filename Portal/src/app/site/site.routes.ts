import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { ContactUsComponent } from "./contactus/contactus.component";
import { QuotationComponent } from "./quotation/quotation.component";
import { AboutUsComponent } from "./aboutus/aboutus.component";
import { Header } from "./header/header";
import { Footer } from "./footer/footer";
import { BienvenidaComponent } from './bienvenida/bienvenida.component';

export const routes: Routes = [
    {
        path: "",
        redirectTo: "home",
        pathMatch: "full"
    },
    {
        path: "home",
        component: HomeComponent
    },
    {
        path: "aboutus",
        component: AboutUsComponent
    },
    {
        path: "contactus",
        component: ContactUsComponent
    },
    {
        path: "quotation",
        component: QuotationComponent
    },
    {
        path: "footer",
        component: Footer
    },
    {
        path: "header",
        component: Header
    },
    {
        path: "bienvenida",
        component: BienvenidaComponent
    }
];