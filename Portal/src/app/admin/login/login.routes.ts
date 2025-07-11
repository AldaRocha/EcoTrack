import { Routes } from "@angular/router";
import { LoginComponent } from "./login/login.component";
import { ReiniciarContrasenaComponent } from "./reiniciarcontrasena/reiniciarcontrasena.component";

export const routes: Routes = [
    {
        path: "",
        redirectTo: "login",
        pathMatch: "full"
    },
    {
        path: "login",
        component: LoginComponent
    },
    {
        path: "reiniciarcontrasena",
        component: ReiniciarContrasenaComponent
    }
]