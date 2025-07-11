import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: "",
        redirectTo: "seguridad",
        pathMatch: "full"
    },
    {
        path: "seguridad",
        loadChildren: () => import("./seguridad/seguridad.routes").then(r => r.routes)
    }
]