import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {CrudService} from "../../services/CrudService";
import {Path} from "../../utilities/Path";
import {User} from "../../models/User";
import {Emprestimo} from "../../models/Emprestimo";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-emprestimos',
  templateUrl: './pagina-emprestimos.component.html',
  styleUrls: ['./pagina-emprestimos.component.css']
})
export class PaginaEmprestimosComponent implements OnInit{
  constructor(
    private router: Router,
    private userService: CrudService<User>,
    private confirmationService: ConfirmationService,
    private emprestimoService: CrudService<Emprestimo>,
    private messageService: MessageService
  ) {
  }
  NavigateOnClick(){
    this.router.navigate(['/pagina-emprestimos-cadastro']);
  }

  private destroy$ = new Subject();
  listaEmprestimos: Emprestimo[] = [];
  totalRecords: any = 0;
  page = 0;
  size = 10;
  sort: string = "";
  direction: string = "DESC";


  ngOnInit(): void {
    this.loadTable();
  }

  ngAfterViewInit(): void {
    if (history.state) {
      const message = history.state.message;
      if (message) {
        this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: message });
        history.replaceState(null, '');
      }
    }
  }

  loadTable(): void {

    // Faça a chamada para carregar os livros e assine o Observable.
    this.userService.listAll(Path.LOCALHOST + '/emprestimo/listAll', this.page, this.size, this.sort, this.direction )
      .pipe(takeUntil(this.destroy$))
      .subscribe((res: any) => {
        this.listaEmprestimos = res.content;
        this.totalRecords = res.totalElements;
      });
  }

  onPageChange(event: any) {
    this.page = event.first / event.rows;
    this.loadTable(); // Recarregue os dados da página atual.
  }

  estenderEmprestimo(event: Event, item: Emprestimo) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja estender?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => {
        this.emprestimoService.extend(Path.LOCALHOST + '/emprestimo/estender', item.id).subscribe(success => {
            this.loadTable();
            this.messageService.add({
              severity: 'success', summary: 'Sucesso', detail: 'Prazo do empréstimo de id: ' + item.id + ' estendido com sucesso!'
            });
          },
          error => {
            let errorMessage: string;

            if (error.error) {
              errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
            } else {
              errorMessage = "Não foi possível cadastrar o usuário!";
            }

            this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
          },
          () => {
            this.loadTable();
          }
        );
      },
      reject: () => {
        console.log("Não foi possível estender o empréstimo de id: " + item.id);
      }
    });
  }

  devolverEmprestimo(event: Event, item: Emprestimo) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja devolver?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => {
        this.emprestimoService.return(Path.LOCALHOST + '/emprestimo/devolucao', item.id).subscribe(success => {
            this.loadTable();
            this.messageService.add({
              severity: 'success', summary: 'Sucesso', detail: 'Devolução do Empréstimo de id: ' + item.id + ' realizada!'
            });
          },
          error => {
            let errorMessage: string;

            if (error.error) {
              errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
            } else {
              errorMessage = "Não foi possível devolver o empréstimo de id: " + item.id;
            }

            this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
          },
          () => {
            console.log("Não foi possível devolver o empréstimo de id: " + item.id);
          }
        );
      },
      reject: () => {
        console.log("Não foi possível devolver o empréstimo de id: " + item.id);
      }
    });
  }

  paginacao(event: any): void {
    this.page = event.first / this.size;
    this.sort = event.sortField;
    this.direction = event.sortOrder;
    this.loadTable();
  }
}
