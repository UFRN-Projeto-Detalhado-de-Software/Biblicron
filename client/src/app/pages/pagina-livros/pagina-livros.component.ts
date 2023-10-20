import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Livro} from "../../models/Livro";
import {CrudService} from "../../services/CrudService";
import {Path} from "../../utilities/Path";
import {Subject, takeUntil} from "rxjs";
import {HttpParams} from "@angular/common/http";

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
  size = 4;
  order: string = "id";
  direction: string = "DESC";
  constructor(
    private router: Router,
    private livroService: CrudService<Livro>,
  ) {
  }

  NavigateOnClick(){
    this.router.navigate(['/pagina-livros-cadastro']);
  }

  ngOnInit(): void {
    this.loadTable();
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
}
