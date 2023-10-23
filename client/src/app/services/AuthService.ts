import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedIn(): boolean {
    // Verifique se o usuário está logado (por exemplo, verificando a existência de informações de usuário no Local Storage)
    const userJson = localStorage.getItem('currentUser');
    return !!userJson;
  }
}
