import { Injectable } from "@angular/core";

@Injectable()
export class StorageService{
    Perfix: string = "HM0987";
    Parts: any = {};

    constructor(){
        if (localStorage.getItem(this.Perfix + "_MJSDUDF76dHDGFnd")){
            this.Parts = JSON.parse(this.BinaryToString(localStorage.getItem(this.Perfix + "_MJSDUDF76dHDGFnd")));
        }
    }

    BinaryToString(input: any): any{
        let bytesLeft = input;
        let result = "";
        while(bytesLeft.length){
            const byte = bytesLeft.substr(0, 8);
            bytesLeft = bytesLeft.substr(8);
            result += String.fromCharCode(parseInt(byte, 2));
        }
        return result;
    }

    SetItem(name: any, value: any){
        let parts = value.split('.');
        this.Parts[name] = [];
        parts.map((part: any, i: any) => {
            if(i > 0){
                part = '.' + part;
            }
            let token = this.MakeId(20);
            this.Parts[name].push(this.GetName(token));
            localStorage.setItem(this.GetName(token), this.StringToBinary(part));
        });
        this.Save();
    }

    MakeId(length: any){
        var result = '';
        var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        var charactersLength = characters.length;
        for (var i = 0; i < length; i++){
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    }

    GetName(name: any): any{
        return this.Perfix + "_" + name;
    }

    StringToBinary(input: any): any{
        var characters = input.split('');
        return characters.map(function(char: any) {
            const binary = char.charCodeAt(0).toString(2)
            const pad = Math.max(8 - binary.length, 0);
            return '0'.repeat(pad) + binary;
        }).join('');
    }

    Save(){
        let data = Object.assign({}, localStorage);
        localStorage.clear();
        Object.keys(data).map(storage => {
            Object.keys(this.Parts).map(part => {
                this.Parts[part].map((key: any) => {
                    if (key == storage){
                        localStorage.setItem(storage, data[storage]);
                    }
                });
            });
        });
        localStorage.setItem(this.Perfix + '_MJSDUDF76dHDGFnd', this.StringToBinary(JSON.stringify(this.Parts)));
    }

    GetItem(name: any): any{
        let chunks = this.GetChunk(name);
        if (!chunks){
            return null;
        }
        let binary = '';
        chunks.map((chunk: any) => {
            binary += localStorage.getItem(chunk)
        });
        return this.BinaryToString(binary);
    }

    GetChunk(name: any): any{
        if (localStorage.getItem(this.Perfix + '_MJSDUDF76dHDGFnd')){
            let parse = JSON.parse(this.BinaryToString(localStorage.getItem(this.Perfix + '_MJSDUDF76dHDGFnd')));
            if (parse[name]){
                return parse[name]
            }
        }
        return null;
    }

    RemoveItem(name: any){
        let chunks = this.GetChunk(name);
        if (chunks){
            delete this.Parts[name];
            chunks.map((chunk: any) => {
                localStorage.removeItem(chunk);
            });
        }
        this.Save();
    }

    Clear(){
        this.Parts = {};
        localStorage.clear();
    }
}