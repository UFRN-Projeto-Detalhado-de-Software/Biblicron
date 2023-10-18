import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-pagina-emprestimos',
  templateUrl: './pagina-emprestimos.component.html',
  styleUrls: ['./pagina-emprestimos.component.css']
})
export class PaginaEmprestimosComponent {
  constructor(private router: Router) {
  }
  NavigateOnClick(){
    this.router.navigate(['/pagina-emprestimos-cadastro']);
  }
}
