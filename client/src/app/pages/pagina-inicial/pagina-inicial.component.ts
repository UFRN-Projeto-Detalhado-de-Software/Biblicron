import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";

@Component({
  selector: 'app-pagina-inicial',
  templateUrl: './pagina-inicial.component.html',
  styleUrls: ['./pagina-inicial.component.css']
})
export class PaginaInicialComponent implements OnInit{

  ngOnInit() {
    console.log(this.getUserFromLocalStorage());
  }

  getUserFromLocalStorage(): User | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }

}
