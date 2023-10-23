import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CleanupService {
  constructor() {
    window.addEventListener('unload', this.cleanupLocalStorage);
  }

  cleanupLocalStorage() {
    localStorage.removeItem('currentUser');
    // Adicione outras chaves do Local Storage que deseja limpar aqui, se necessário.
  }
}
