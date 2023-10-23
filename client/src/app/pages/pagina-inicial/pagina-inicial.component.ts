import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {Router} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {CrudService} from "../../services/CrudService";

@Component({
  selector: 'app-pagina-inicial',
  templateUrl: './pagina-inicial.component.html',
  styleUrls: ['./pagina-inicial.component.css']
})
export class PaginaInicialComponent implements OnInit{
  loggedInUser: User | null;

  constructor(
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
  ) {
    this.loggedInUser = new User();
  }

  ngOnInit() {
    this.loggedInUser = this.getUserFromLocalStorage(); // Obter informações do usuário
    console.log(this.loggedInUser);

  }

  ngAfterViewInit(){
    if(history.state){
      const message = history.state.message;
      if(message){
        this.messageService.add({severity: 'sucess', summary: 'Sucesso', detail: message});
        history.replaceState(null, '');
      }
    }
  }

  getUserFromLocalStorage(): User | null {
    const userJson = localStorage.getItem('currentUser');
    return userJson ? JSON.parse(userJson) : null;
  }

}
