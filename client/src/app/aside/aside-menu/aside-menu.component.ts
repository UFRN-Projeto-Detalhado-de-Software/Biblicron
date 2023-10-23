import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-aside-menu',
  templateUrl: './aside-menu.component.html',
  styleUrls: ['./aside-menu.component.css']
})
export class AsideMenuComponent {

  constructor(private router: Router) {
  }
  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/pagina-login']);
  }
}
