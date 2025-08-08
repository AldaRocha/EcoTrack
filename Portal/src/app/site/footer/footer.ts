import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class Footer {
  email: string = '';
  aceptaNotificaciones: boolean = false;

  constructor(public router: Router) { }

  navigateTo(path: string) {
    this.router.navigateByUrl('/ecodev' + path);
  }

  guardarSuscripcion() {
    if (!this.email || this.email.trim() === '') {
      Swal.fire({
        icon: 'warning',
        title: 'Falta el email',
        text: 'Por favor ingresa un email válido.'
      });
      return;
    }

    const datos = {
      email: this.email,
      acepta: this.aceptaNotificaciones,
      fecha: new Date().toISOString()
    };
    console.log('Email capturado:', this.email);

    const suscripciones = JSON.parse(localStorage.getItem('suscripcionesEcoDev') || '[]');
    suscripciones.push(datos);
    localStorage.setItem('suscripcionesEcoDev', JSON.stringify(suscripciones));

    Swal.fire({
      icon: 'success',
      title: '¡Suscripción guardada!',
      text: this.aceptaNotificaciones
        ? 'Gracias por aceptar recibir nuestras noticias.'
        : 'Guardamos tu correo. Puedes activar notificaciones más adelante.'
    });

    this.email = '';
    this.aceptaNotificaciones = false;
  }

}
