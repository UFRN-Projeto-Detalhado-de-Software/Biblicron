import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-pagina-livros',
  templateUrl: './pagina-livros.component.html',
  styleUrls: ['./pagina-livros.component.css']
})
export class PaginaLivrosComponent {
  constructor(private router: Router) {
  }

  NavigateOnClick(){
    this.router.navigate(['/pagina-livros-cadastro']);
  }
}
