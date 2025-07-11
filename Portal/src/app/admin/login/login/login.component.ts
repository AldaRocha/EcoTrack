import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";

@Component({
    selector: "app-login",
    templateUrl: "./login.component.html",
    styleUrls: ["./login.component.css"],
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
    ]
})
export class LoginComponent implements OnInit   {
    FormLogin = new FormGroup({});
    passwordVisible: Boolean = false;

    constructor(private _fb: FormBuilder){

    }

    ngOnInit(): void {
        // this.FormLogin = this._fb.group({

        // });
    }

    async Login(){

    }

    TogglePasswordVisibility(){
        this.passwordVisible = !this.passwordVisible;
    }
}