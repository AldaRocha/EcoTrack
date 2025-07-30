import { CommonModule } from "@angular/common";
import { Component, OnInit  } from "@angular/core";
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Header } from "../header/header";
import { Footer } from "../footer/footer";

@Component({
    selector: "app-contactus",
    templateUrl: "./contactus.component.html",
    styleUrls: ["./contactus.component.css"],
    standalone: true,
    imports: [
        CommonModule,
        Header,
        ReactiveFormsModule, 
        Footer
    ]
})
export class ContactUsComponent implements OnInit{
    form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: [''],
      apellido: [''],
      compania: [''],
      pais: [''],
      correo: [''],
      aceptaComunicaciones: [false]
    });
  }
  onSubmit(): void {
    if (this.form.valid) {
      console.log('Datos del formulario:', this.form.value);
      localStorage.setItem('formularioContacto', JSON.stringify(this.form.value));
      this.form.reset();
    }
  }
}