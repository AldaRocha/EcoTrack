import { Routes } from "@angular/router";
import { InicioComponent } from "../modulos/inicio/inicio.component";
import { MenuComponent } from "./menu.component";

export const routes: Routes = [
    {
        path: "",
        component: MenuComponent,
        children: [
            {
                path: "inicio",
                component: InicioComponent
            },
            {
                path: "sistema",
                loadChildren: () => import("../modulos/sistema/sistema.routes").then(r => r.routes)
            }
        ]
    }
]