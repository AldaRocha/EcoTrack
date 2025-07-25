import { ApplicationConfig, provideZoneChangeDetection } from "@angular/core";
import { provideRouter } from "@angular/router";
import { routes } from "./app.routes";
import { provideAnimations } from "@angular/platform-browser/animations";
import { JwtHelperService } from "@auth0/angular-jwt";
import { provideToastr } from "ngx-toastr";
import { provideHttpClient } from "@angular/common/http";

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection(
            {
                eventCoalescing: true
            }
        ),
        provideRouter(
            routes
        ),
        provideAnimations(),
        JwtHelperService,
        provideToastr(),
        provideHttpClient()
    ]
}