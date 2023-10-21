import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Subject, takeUntil} from "rxjs";
import {Livro} from "../../models/Livro";
import {CrudService} from "../../services/CrudService";
import {Path} from "../../utilities/Path";
import {User} from "../../models/User";
import {Emprestimo} from "../../models/Emprestimo";

@Component({
  selector: 'app-pagina-emprestimos',
  templateUrl: './pagina-emprestimos.component.html',
  styleUrls: ['./pagina-emprestimos.component.css']
})
export class PaginaEmprestimosComponent implements OnInit{
  constructor(
    private router: Router,
    private userService: CrudService<User>) {
  }
  NavigateOnClick(){
    this.router.navigate(['/pagina-emprestimos-cadastro']);
  }

  private destroy$ = new Subject();
  listaEmprestimos: Emprestimo[] = [];
  totalRecords: any = 0;
  page = 0;
  size = 10;
  order: string = "id";
  direction: string = "DESC";


  ngOnInit(): void {
    this.loadTable();
  }

  loadTable(): void {

    // Faça a chamada para carregar os livros e assine o Observable.
    this.userService.listAll(Path.LOCALHOST + '/emprestimo/listAll', this.page, this.size, this.order, this.direction )
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
}
