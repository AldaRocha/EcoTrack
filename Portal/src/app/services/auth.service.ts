import { Injectable } from "@angular/core";
import { StorageService } from "./storage.service";
import { JwtHelperService } from "@auth0/angular-jwt";
import { RestService } from "./rest.service";
import { Router } from "@angular/router";
import { AuthData } from "../models/admin/AuthData";
import { Seguridad } from "../models/admin/Seguridad";

@Injectable()
export class AuthService{
    constructor(
        public storage: StorageService,
        public jwt: JwtHelperService,
        public _rest: RestService,
        public _router: Router
    ){

    }

    GetAuthData(){
        if (this.storage.GetItem("ecotrack-oauth")){
            return new AuthData(
                this.jwt.decodeToken(
                    this.storage.GetItem("ecotrack-oauth")
                )
            );
        } else{
            return new AuthData();
        }
    }

    GetRuta(){
        let pantallas = this._rest.Decode(
            this.storage.GetItem('HM43')
        );
        let p = pantallas ? pantallas.find((x: any) => 
            this._router.url.includes(x.Ruta)
        ) : null;
        if (!p){
            return {
                nombre: null,
                ruta: null,
                identificador: null
            };
        }
        return {
            nombre: p.Nombre,
            ruta: [
                {
                    icono: p.ModuloIcono,
                    nombre: p.Modulo
                },
                {
                    icono: p.Icono,
                    nombre: p.Nombre
                }
            ],
            identificador: p.Identificador
        };
    }

    async GetPermisos(){
        let data = await new Promise((resolve, reject) => {
            setTimeout(() => {
                let datas: any = {};
                let permisosData = this._rest.Decode(this.storage.GetItem('HM43'));
                let permisoD = permisosData.find((x: any) => this._router.url.includes(x.Ruta));
                if (permisoD){
                    let id = permisoD.Identificador;
                    let permisos = permisosData.find((x: any) => x.Identificador == id);
                    if (permisos){
                        permisos.Permisos.map((permiso: any) => {
                            datas[permiso.Identificador] = true;
                        });
                    }
                }
                let pass = false;
                Object.keys(datas).map(key => {
                    if (datas[key] == true){
                        pass = true;
                    }
                });
                if (!pass){
                    this._router.navigate(['/menu/inicio']);
                }
                resolve(datas);
            }, 100);
        });
        return new Seguridad(data);
    }
}