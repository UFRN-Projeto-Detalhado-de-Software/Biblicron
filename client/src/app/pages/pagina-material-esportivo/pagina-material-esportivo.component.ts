import {Component, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {Livro} from "../../models/Livro";
import {Router} from "@angular/router";
import {CrudService} from "../../services/CrudService";
import {ConfirmationService, MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";
import {MaterialEsportivo} from "../../models/MaterialEsportivo";

@Component({
  selector: 'app-pagina-material-esportivo',
  templateUrl: './pagina-material-esportivo.component.html',
  styleUrls: ['./pagina-material-esportivo.component.css']
})
export class PaginaMaterialEsportivoComponent implements OnInit{
  private destroy$ = new Subject();
  listaMaterialEsportivo: MaterialEsportivo[] = [];
  totalRecords: any = 0;
  page = 0;
  size = 10;
  order: string = "nomeProduto";
  direction: string = "DESC";
  constructor(
    private router: Router,
    private materialEsportivoService: CrudService<MaterialEsportivo>,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
  }

  NavigateOnClick(){
    this.router.navigate(['/pagina-material-esportivo-cadastro']);
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
    this.materialEsportivoService.listAll(Path.LOCALHOST + '/material-esportivo/listAll', this.page, this.size, this.order, this.direction)
      .pipe(takeUntil(this.destroy$))
      .subscribe((res: any) => {
        this.listaMaterialEsportivo = res.content;
        this.totalRecords = res.totalElements;
      });
  }

  onPageChange(event: any) {
    this.page = event.first / event.rows;
    this.loadTable(); // Recarregue os dados da página atual.
  }

  confirmarRemocao(event: Event, item: MaterialEsportivo) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja remover?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => {
        this.materialEsportivoService.delete(Path.LOCALHOST + '/material-esportivo/delete', item.id).subscribe(success => {
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
        console.log("Não foi possível remover o material esportivo de id: " + item.id);
      }
    });
  }

  editItem(materialEsportivo: MaterialEsportivo) {
    this.router.navigate(['/pagina-material-esportivo-cadastro'], { state: { materialEsportivo } });
  }

  paginacao(event: any): void {
    this.page = event.first / this.size;
    this.order = event.sortField;
    this.direction = event.sortOrder;
    this.loadTable();
  }
}
