import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-pagina-usuarios',
  templateUrl: './pagina-usuarios.component.html',
  styleUrls: ['./pagina-usuarios.component.css']
})
export class PaginaUsuariosComponent {
  constructor(private router: Router) {
  }
  NavigateOnClick(){
    this.router.navigate(['/pagina-usuarios-cadastro']);
  }
}
