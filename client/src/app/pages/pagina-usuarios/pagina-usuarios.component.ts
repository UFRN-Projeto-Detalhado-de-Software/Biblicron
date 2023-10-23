import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {ConfirmationService, MessageService} from "primeng/api";
import {CrudService} from "../../services/CrudService";
import {User} from "../../models/User";
import {Path} from "../../utilities/Path";
import {Subject, takeUntil} from "rxjs";

@Component({
  selector: 'app-pagina-usuarios',
  templateUrl: './pagina-usuarios.component.html',
  styleUrls: ['./pagina-usuarios.component.css']
})
export class PaginaUsuariosComponent implements OnInit{

  private destroy$ = new Subject();
  page = 0;
  size = 10;
  totalRecords = 0;
  order = "username";
  direction = "";
  listaUsers: User[] = [];
  constructor(
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private userService: CrudService<User>
  ) {
  }

  ngOnInit() {
    this.loadTable();
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

  NavigateOnClick(){
    this.router.navigate(['/pagina-usuarios-cadastro']);
  }

  loadTable(){
    this.userService.listAll(Path.LOCALHOST + '/user/listAll', this.page, this.size, this.order, this.direction).pipe(takeUntil(this.destroy$))
      .subscribe((res: any) => {
        this.listaUsers = res.content;
        this.totalRecords = res.totalElements;
      })
  }

  onPageChange(event: any){
    this.page = event.first / event.rows;
    this.loadTable();
  }

  confirmarRemocao(event: Event, item: User){
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja remover?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () =>{
        this.userService.delete(Path.LOCALHOST + '/user/delete', item.id).subscribe(success =>{
          this.loadTable();
        },
          error => {
            this.loadTable();
          },

          () => {
            this.loadTable();
          }
          );
      },
      reject: () => {
        console.log("Não foi possível remover o user de id: " + item.id);
      }
    })
  }

  editItem(user: User){
    this.router.navigate(['pagina-usuarios-cadastro'], {state: { user }});
  }

  paginacao(event: any): void {
    this.page = event.first / this.size;
    this.order = event.sortField;
    this.direction = event.sortOrder;
    this.loadTable();
  }
}
