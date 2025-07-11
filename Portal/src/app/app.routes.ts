import { Routes } from "@angular/router";

export const routes: Routes = [
    {
        path: "",
        redirectTo: "ecodev",
        pathMatch: "full"
    },
    {
        path: "ecodev",
        loadChildren: () => import("./site/site.routes").then(r => r.routes)
    },
    {
        path: "admin",
        loadChildren: () => import("./admin/admin.routes").then(r => r.routes)
    }
]