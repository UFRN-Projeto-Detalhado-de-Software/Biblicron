import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {NavigationStart, Router} from "@angular/router";
import {AuthService} from "./services/AuthService";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'client';
  hideAsideMenu: boolean = false;

  constructor(private authService: AuthService, private router: Router) {
    if (!this.authService.isLoggedIn()) {
      // Se o usuário não estiver logado, redirecione para a página de login
      this.router.navigate(['/pagina-login']);
    } else {
      // Se o usuário estiver logado, redirecione para a página principal (ou outra página)
      this.router.navigate(['/pagina-inicial']);
    }

    window.onbeforeunload = function() {
      localStorage.removeItem('currentUser');
      return '';
    };
  }

  isUserAuthenticated(): boolean {
    return this.authService.isLoggedIn(); // Implemente esse método em AuthService
  }

  ngOnInit(): void {
  }
}
