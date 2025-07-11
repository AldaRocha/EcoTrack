import { Injectable } from "@angular/core";
import { environment } from "../../environments/environments";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { ToastrService } from "ngx-toastr";
import { StorageService } from "./storage.service";
import { JwtHelperService } from "@auth0/angular-jwt";
import { NgxSpinnerService } from "ngx-spinner";
import { Router } from "@angular/router";
import { AuthData } from "../models/admin/AuthData";
import * as CryptoJS from "crypto-js";

export interface Response{
    message?: string;
    token?: any;
    error?: boolean;
}

@Injectable()
export class RestService{
    RestApi: string = environment.url;
    Version: string = "v1";
    Secret: string = "EcoTrack2025";
    SVP: any = "";

    notify: {
        success: any,
        warning: any,
        error: any,
        clear: any
    } = {
        success: (message: string) => {
            this.toastr.success(message, "¡Bien!", {
                positionClass: "toast-bottom-center"
            });
        },
        warning: (message: string, disableTimeOut = false) => {
            this.toastr.warning(message, '¡Aviso!', {
                positionClass: 'toast-bottom-center',
                disableTimeOut: disableTimeOut,
                enableHtml: true
            });
        },
        error: (message: string) => {
            this.toastr.error(message, '¡Error!', {
                positionClass: 'toast-bottom-center'
            });
        },
        clear: () => {
            this.toastr.clear();
        }
    }

    Headers: HttpHeaders;

    constructor(
        private toastr: ToastrService,
        public storage: StorageService,
        private jwt: JwtHelperService,
        public spinner: NgxSpinnerService,
        private _http: HttpClient,
        public router: Router
    ){
        this.Headers = new HttpHeaders();
        if(this.storage.GetItem("ecotrack-oauth")){
            let data: AuthData = new AuthData(
                this.jwt.decodeToken(
                    this.storage.GetItem("ecotrack-oauth")
                )
            );
            this.Headers = this.Headers.set("Authorization", data.Bearer)
        }
    }

    Post(
        route: any,
        data: any,
        version = true,
        decode = true,
        loading = true
    ){
        this.SetHeaders();
        if(loading){
            this.spinner.show();
        }
        return new Promise((resolve, reject) => {
            let token = this.Sign(data, this.Secret);
            let v = "";
            if (version){
                v = this.Version;
            }
            this._http.post(
                this.RestApi + v + route,
                {
                    token: token
                },
                {
                    headers: this.Headers,
                    observe: "response"
                }
            ).subscribe((data: any) => {
                let message = data.body['message'];
                let token = data.body['token'];
                if(data.status == 200){
                    if(!decode){
                        if(message != null){
                            this.spinner.hide();
                            resolve(
                                {
                                    message: message,
                                    error: true,
                                    data: null
                                }
                            );
                        } else{
                            this.spinner.hide();
                            resolve(token);
                        }
                    } else{
                        this.spinner.hide();
                        resolve(
                            {
                                message: message,
                                data: this.Decode(token)
                            }
                        );
                    }
                } else{
                    this.spinner.hide();
                    this.notify.warning(message);
                    resolve(
                        {
                            message: message,
                            error: true
                        }
                    );
                }
            }, err => {
                if(err.status == 401){
                    this.storage.Clear();
                    this.router.navigate(["admin/login/login"]);
                    this.notify.warning("No tienes permiso para realizar esta petición");
                    resolve(
                        {
                            message: err,
                            error: true
                        }
                    )
                }
                this.spinner.hide();
            });
        });
    }

    SetHeaders(){
        this.Headers = new HttpHeaders();
        if (this.storage.GetItem("ecotrack-oauth")){
            let data: AuthData = new AuthData(
                this.jwt.decodeToken(
                    this.storage.GetItem("ecotrack-oauth")
                )
            );
            this.Headers = this.Headers.set("Authorization", data.Bearer);
        }
    }

    Sign(data: any, secret: any){
        var header = {
            "alg": "HS256",
            "typ": "JWT"
        };
        
        var stringifiedHeader = CryptoJS.enc.Utf8.parse(JSON.stringify(header));
        var encodedHeader = this.Base64url(stringifiedHeader);
        var stringifiedData = CryptoJS.enc.Utf8.parse(JSON.stringify(data));
        var encodedData = this.Base64url(stringifiedData);
        var token = encodedHeader + "." + encodedData;
        var signature = CryptoJS.HmacSHA256(token, secret);
        signature = this.Base64url(signature);
        return token + "." + signature;
    }

    Base64url(source: any): any{
        let encodedSource = CryptoJS.enc.Base64.stringify(source);
        encodedSource = encodedSource.replace(/=+$/, "");
        encodedSource = encodedSource.replace(/\+/g, '-');
        encodedSource = encodedSource.replace(/\//g, '_');
        return encodedSource;
    }

    Decode(token: any){
        return this.jwt.decodeToken(token);
    }

    Get(
        route: any,
        version = true,
        loading = true
    ){
        this.SetHeaders();
        if(loading){
            this.spinner.show();
        }
        return new Promise((resolve, reject) => {
            let v = "";
            if(version){
                v = this.Version;
            }
            this._http.get(
                this.RestApi + v + route,
                {
                    headers: this.Headers,
                    observe: "response"
                }
            ).pipe().subscribe((data: any) => {
                let message = data.body['message'];
                let token = data.body['token'];
                if(data.status == 200){
                    resolve(
                        {
                            message: message,
                            data: this.Decode(token)
                        }
                    );
                } else{
                    this.notify.warning(message);
                    resolve(
                        {
                            error: true,
                            message: message
                        }
                    );
                }
                if(loading){
                    this.spinner.hide();
                }
            }, err => {
                if(err.status == 401){
                    this.storage.Clear();
                    this.router.navigate(["admin/login/login"]);
                    this.notify.warning("No tienes permiso para realizar esta petición");
                    resolve(
                        {
                            message: "No tienes permiso para realizar esta petición",
                            error: true
                        }
                    );
                } else{
                    this.notify.error("No se pudo comunicar con el servidor");
                    console.error(err);
                    resolve(
                        {
                            message: err,
                            error: true
                        }
                    )
                }
                if(loading){
                    this.spinner.hide();
                }
            });
        });
    }

    Put(
        route: any,
        data: any,
        version = true,
        decode = true,
        loading = true
    ){
        this.SetHeaders();
        if (loading){
            this.spinner.show();
        }
        return new Promise((resolve, reject) => {
            let token = this.Sign(data, this.Secret);
            let v = "";
            if (version) {
                v = this.Version;
            }
            this._http.put(
                this.RestApi + v + route,
                {
                    token: token
                },
                {
                    headers: this.Headers,
                    observe: "response"
                }
            ).subscribe((data: any) => {
                let message = data.body['message'];
                let token = data.body['token'];
                if (data.status == 200){
                    if (!decode){
                        resolve(token);
                    } else{
                        resolve(
                            {
                                message: message,
                                data: this.Decode(token)
                            }
                        );
                    }
                } else{
                    this.notify.warning(message);
                    resolve(
                        {
                            message: message,
                            error: true
                        }
                    );
                }
                if (loading){
                    this.spinner.hide();
                }
            }, err => {
                if (err.status == 401){
                    this.storage.Clear();
                    this.router.navigate(['admin/login/login']);
                    this.notify.warning("No tienes permiso para realizar esta petición");
                    resolve(
                        {
                            message: "No tienes permiso para realizar esta petición",
                            error: true
                        }
                    )
                } else{
                    this.notify.error("No se pudo comunicar con el servidor");
                    console.log(err);
                    resolve(
                        {
                            message: err,
                            error: true
                        }
                    );
                }
                if (loading){
                    this.spinner.hide();
                }
            });
        });
    }

    Delete(
        route: any,
        version: any = true,
        decode = true
    ){
        this.SetHeaders();
        this.spinner.show();
        return new Promise((resolve, reject) => {
            let v = '';
            if (version) {
                v = this.Version;
            }
            this._http.delete(
                this.RestApi + v + route,
                {
                    headers: this.Headers,
                    observe: "response"
                }
            ).subscribe((data: any) => {
                let message = data.body['message'];
                let token = data.body['token'];
                if (data.status == 200){
                    if (!decode){
                        resolve(token);
                    } else{
                        resolve(
                            {
                                message: message,
                                data: this.Decode(token)
                            }
                        );
                    }
                } else{
                    this.notify.warning(message);
                    resolve(
                        {
                            message: message,
                            error: true
                        }
                    );
                }
                this.spinner.hide();
            }, err => {
                if (err.status == 401){
                    this.storage.Clear();
                    this.router.navigate(['admin/login/login']);
                    this.notify.warning("No tienes permiso para realizar esta petición");
                    resolve(
                        {
                            message: "No tienes permiso para realizar esta petición",
                            error: true
                        }
                    );
                } else{
                    this.notify.error("No se pudo comunicar con el servidor");
                    console.log(err);
                    resolve(
                        {
                            message: err,
                            error: true
                        }
                    );
                }
                this.spinner.hide();
            });
        });
    }
}