import { Injectable } from "@angular/core";

declare var $: any;

@Injectable()
export class HelperService{
    constructor(){

    }

    PaginaUp(){
        $("html, body").scrollTop(0);
    }

    SelectError(elemento: any, modal: boolean = true): any{
        if (!elemento.length){
            return false;
        }
        elemento.css("box-shadow", "0px, 0px, 13px, 0px rgb(255, 255, 0)");
        setTimeout(function(){
            elemento.css("box-shadow", "none");
        }, 3000);
        if (modal){
            $('html, body').animate(
                {
                    scrollTop: elemento.offset().top - 50
                }, 1000
            );
        }
    }
}