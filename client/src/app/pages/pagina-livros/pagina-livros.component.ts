import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Livro} from "../../models/Livro";
import {CrudService} from "../../services/CrudService";
import {Path} from "../../utilities/Path";
import {Subject, takeUntil} from "rxjs";
import {HttpParams} from "@angular/common/http";
import {ConfirmationService, Message, MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-livros',
  templateUrl: './pagina-livros.component.html',
  styleUrls: ['./pagina-livros.component.css']
})
export class PaginaLivrosComponent implements OnInit{
  private destroy$ = new Subject();
  listaLivros: Livro[] = [];
  totalRecords: any = 0;
  page = 0;
  size = 10;
  order: string = "id";
  direction: string = "DESC";
  constructor(
    private router: Router,
    private livroService: CrudService<Livro>,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
  }

  NavigateOnClick(){
    this.router.navigate(['/pagina-livros-cadastro']);
  }

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
    this.livroService.listAll(Path.LOCALHOST + '/livro/listAll', this.page, this.size, this.order, this.direction)
      .pipe(takeUntil(this.destroy$))
      .subscribe((res: any) => {
        this.listaLivros = res.content;
        this.totalRecords = res.totalElements;
      });
  }

  onPageChange(event: any) {
    this.page = event.first / event.rows;
    this.loadTable(); // Recarregue os dados da página atual.
  }

  confirmarRemocao(event: Event, item: Livro) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja remover?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => {
        this.livroService.delete(Path.LOCALHOST + '/livro/delete', item.id).subscribe(success => {
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
        console.log("Não foi possível remover o livro de id: " + item.id);
      }
    });
  }

  editItem(livro: Livro) {
    this.router.navigate(['/pagina-livros-cadastro'], { state: { livro } });
  }

  paginacao(event: any): void {
    this.page = event.first / this.size;
    this.order = event.sortField;
    this.direction = event.sortOrder;
    this.loadTable();
  }
}
