import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { environment } from '../../../environments/environments';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [FormsModule, HttpClientModule, CommonModule, ReactiveFormsModule],
  templateUrl: './footer.html',
  styleUrls: ['./footer.css']
})
export class Footer {
  subscriptionForm: FormGroup;
  isSubmitting: boolean = false;

  constructor(
    public router: Router,
    private http: HttpClient,
    private fb: FormBuilder
  ) {
    this.subscriptionForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      aceptaNotificaciones: [false]
    });
  }

  navigateTo(path: string) {
    this.router.navigateByUrl('/ecodev' + path);
    window.scrollTo(0, 0);
  }

  guardarSuscripcion() {
    // Verificar si el formulario es válido
    if (this.subscriptionForm.invalid) {
      this.mostrarErrorValidacion();
      return;
    }

    this.isSubmitting = true;

    const subscriptionData = {
      email: this.subscriptionForm.value.email,
      acceptsNotifications: this.subscriptionForm.value.aceptaNotificaciones
    };

    this.http.post('http://localhost:5063/api/v1/subscription', subscriptionData)
      .subscribe({
        next: (response: any) => {
          this.mostrarExito(response);
          this.resetForm();
        },
        error: (error) => {
          this.manejarError(error);
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
  }

  private mostrarErrorValidacion() {
    if (this.subscriptionForm.get('email')?.errors?.['required']) {
      Swal.fire({
        icon: 'warning',
        title: 'Email requerido',
        text: 'Por favor ingresa tu dirección de correo electrónico',
        confirmButtonColor: '#3d5af1'
      });
    } else if (this.subscriptionForm.get('email')?.errors?.['email']) {
      Swal.fire({
        icon: 'warning',
        title: 'Email inválido',
        text: 'Por favor ingresa una dirección de correo electrónico válida',
        confirmButtonColor: '#3d5af1'
      });
    }
  }

  private mostrarExito(response: any) {
    Swal.fire({
      icon: 'success',
      title: '¡Suscripción exitosa!',
      html: this.subscriptionForm.value.aceptaNotificaciones
        ? `Gracias por suscribirte a nuestras notificaciones. Hemos enviado un correo de confirmación a <strong>${this.subscriptionForm.value.email}</strong>.`
        : `Gracias por registrarte. Hemos enviado un correo a <strong>${this.subscriptionForm.value.email}</strong> con instrucciones para activar notificaciones cuando lo desees.`,
      confirmButtonColor: '#30CB3D',
      showClass: {
        popup: 'animate__animated animate__fadeInDown'
      },
      hideClass: {
        popup: 'animate__animated animate__fadeOutUp'
      }
    });
  }

  private manejarError(error: any) {
    let errorMessage = 'Ocurrió un error al procesar tu suscripción';
    
    if (error.error) {
      // Error del servidor (400, 409, 500, etc.)
      if (error.error.message) {
        errorMessage = error.error.message;
      }
      
      // Manejo específico para conflictos (email duplicado)
      if (error.status === 409) {
        errorMessage = error.error.message || 'Este email ya está registrado';
      }
    } else if (error.status === 0) {
      // Error de conexión
      errorMessage = 'No se pudo conectar con el servidor. Verifica tu conexión a internet.';
    }

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: errorMessage,
      confirmButtonColor: '#3d5af1',
      showClass: {
        popup: 'animate__animated animate__headShake'
      }
    });
  }

  private resetForm() {
    this.subscriptionForm.reset({
      email: '',
      aceptaNotificaciones: false
    });
  }
}