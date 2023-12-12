import {Component, OnInit} from '@angular/core';
import {Subject, takeUntil} from "rxjs";
import {MaterialEsportivo} from "../../models/MaterialEsportivo";
import {Router} from "@angular/router";
import {CrudService} from "../../services/CrudService";
import {ConfirmationService, MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";
import {Livro} from "../../models/Livro";
import {DispositivoEletronico} from "../../models/DispositivoEletronico";

@Component({
  selector: 'app-pagina-dispositivo-eletronico',
  templateUrl: './pagina-dispositivo-eletronico.component.html',
  styleUrls: ['./pagina-dispositivo-eletronico.component.css']
})
export class PaginaDispositivoEletronicoComponent implements OnInit{
  private destroy$ = new Subject();
  listaDispositivoEletronico: DispositivoEletronico[] = [];
  totalRecords: any = 0;
  page = 0;
  size = 10;
  order: string = "nomeProduto";
  direction: string = "DESC";
  constructor(
    private router: Router,
    private dispositivoEletronicoService: CrudService<DispositivoEletronico>,
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {
  }

  NavigateOnClick(){
    this.router.navigate(['/pagina-dispositivo-eletronico-cadastro']);
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
    this.dispositivoEletronicoService.listAll(Path.LOCALHOST + '/dispositivo-eletronico/listAll', this.page, this.size, this.order, this.direction)
      .pipe(takeUntil(this.destroy$))
      .subscribe((res: any) => {
        this.listaDispositivoEletronico = res.content;
        this.totalRecords = res.totalElements;
      });
  }

  onPageChange(event: any) {
    this.page = event.first / event.rows;
    this.loadTable(); // Recarregue os dados da página atual.
  }

  confirmarRemocao(event: Event, item: DispositivoEletronico) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Tem certeza que deseja remover?',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept: () => {
        this.dispositivoEletronicoService.delete(Path.LOCALHOST + '/dispositivo-eletronico/delete', item.id).subscribe(success => {
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
        console.log("Não foi possível remover o dispositivo eletrônico de id: " + item.id);
      }
    });
  }

  editItem(dispositivoEletronico: DispositivoEletronico) {
    this.router.navigate(['/pagina-dispositivo-eletronico-cadastro'], { state: { dispositivoEletronico } });
  }

  paginacao(event: any): void {
    this.page = event.first / this.size;
    this.order = event.sortField;
    this.direction = event.sortOrder;
    this.loadTable();
  }
}
