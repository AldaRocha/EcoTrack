import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root' // Esto hace que el servicio sea singleton y accesible en toda la app
})
export class DataService {
  private contactSubmissions: any[] = [];
  private quotationSubmissions: any[] = [];
  private subscriptions: any[] = [];

  constructor() { }

  // Métodos para contactos
  addContactSubmission(data: any): void {
    this.contactSubmissions.push({...data, fecha: new Date().toISOString()});
  }

  getContactSubmissions(): any[] {
    return [...this.contactSubmissions];
  }

  // Métodos para cotizaciones
  addQuotationSubmission(data: any): void {
    this.quotationSubmissions.push({...data, fecha: new Date().toISOString()});
  }

  getQuotationSubmissions(): any[] {
    return [...this.quotationSubmissions];
  }

  // Métodos para suscripciones
  addSubscription(data: any): void {
    this.subscriptions.push({...data, fecha: new Date().toISOString()});
  }

  getSubscriptions(): any[] {
    return [...this.subscriptions];
  }
}