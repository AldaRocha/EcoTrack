import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: "",
        redirectTo: "login",
        pathMatch: "full"
    },
    {
        path: "login",
        loadChildren: () => import("./login/login.routes").then(r => r.routes)
    },
    {
        path: "menu",
        loadChildren: () => import("./menu/menu.routes").then(r => r.routes)
    }
]