import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import Swal from 'sweetalert2';
import { Header } from "../header/header";
import { Footer } from "../footer/footer";
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: "app-quotation",
  templateUrl: "./quotation.component.html",
  styleUrls: ["./quotation.component.css"],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    Header, 
    Footer
  ]
})
export class QuotationComponent {

  
  quotationForm = new FormGroup({
    nombre: new FormControl('', Validators.required),
    telefono: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    productoIoT: new FormControl(false),
    apiIA: new FormControl(false),
    usoPlataforma: new FormControl(false),
  });

  onSubmit() {
  if (this.quotationForm.valid) {
    const formValues = this.quotationForm.value;

    const convertedValues = {
      ...formValues,
      productoIoT: formValues.productoIoT ? 'Sí' : 'No',
      apiIA: formValues.apiIA ? 'Sí' : 'No',
      usoPlataforma: formValues.usoPlataforma ? 'Sí' : 'No'
    };

    console.log("Datos guardados:", convertedValues);

    Swal.fire({
      icon: 'success',
      title: '¡Éxito!',
      text: 'Formulario enviado correctamente.',
      confirmButtonColor: '#0db920',
      confirmButtonText: 'Aceptar'
    });

    this.quotationForm.reset();
  } else {
    Swal.fire({
      icon: 'warning',
      title: 'Campos incompletos',
      text: 'Por favor completa todos los campos obligatorios.',
      confirmButtonColor: '#f0ad4e',
      confirmButtonText: 'Ok'
    });
  }
}

}
