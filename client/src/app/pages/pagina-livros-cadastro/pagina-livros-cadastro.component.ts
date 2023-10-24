import {Component, OnInit} from '@angular/core';
import {Observable, Subject} from "rxjs";
import {Genero, Livro} from "../../models/Livro";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {UserType} from "../../models/User";
import {Path} from "../../utilities/Path";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-livros-cadastro',
  templateUrl: './pagina-livros-cadastro.component.html',
  styleUrls: ['./pagina-livros-cadastro.component.css'],
  providers: [CrudService]
})
export class PaginaLivrosCadastroComponent implements OnInit{
  private destroy$ = new Subject();

  // @ts-ignore
  public livroForm: FormGroup;
  errorMessage: string = '';
  livro: Livro;
  generoLivro: any[] = [
    { name: 'Ficção Científica', value: 'FICCAO_CIENTIFICA' },
    { name: 'Fantasia', value: 'FANTASIA' },
    { name: 'Romance', value: 'ROMANCE' },
    { name: 'Suspense', value: 'SUSPENSE' },
    { name: 'Aventura', value: 'AVENTURA' },
    { name: 'Biografia', value: 'BIOGRAFIA' },
    { name: 'Didáticos', value: 'DIDATICOS' },
    { name: 'História em Quadrinhos', value: 'HISTORIA_EM_QUADRINHOS' },
    { name: 'Clássicos', value: 'CLASSICOS' }
  ];



  constructor(
    private livroService: CrudService<Livro>,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient,
    private messageService: MessageService,
  ) {
    this.livro = new Livro();
    this.initForm();
  }

  initForm(): void{
    this.livroForm = this.fb.group({
      id: [null],
      titulo: ['', Validators.required],
      autor: ['', Validators.required],
      quantidade: ['', Validators.required],
      quantidadeDisponivel: ['', Validators.required],
      dataPublicacao: ['', Validators.required],
      paginas: ['', Validators.required],
      generos: [[]],
    });
  }

  ngOnInit(): void {
    const state = history.state; // Obtém o estado da rota
    if (state && state.livro) {
      const livro: Livro = state.livro;
      const id = livro.id;
      if (id) {
        this.handleLivro(livro);
      }
    }
  }

  updateForm(livro: Livro) {
    this.livroForm.patchValue({
      id: livro.id,
      titulo: livro.titulo,
      autor: livro.autor,
      quantidade: livro.quantidade,
      quantidadeDisponivel: livro.quantidadeDisponivel,
      dataPublicacao: livro.dataPublicacao,
      paginas: livro.paginas,
      generos: livro.generos
    });

  }

  private handleLivro(livro: Livro) {
    if (livro) {
      this.livro = livro;
      this.updateForm(livro);
    }
  }

  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.livroForm.markAllAsTouched();
  }

  submit() {
    if (!this.livroForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.livroForm.value;
    this.livro = new Livro(formValues);

    this.submitLivro();
    return;
  }

  submitLivro() {
    if (!this.livroForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.livroForm.value;
    this.livro = new Livro(formValues);
    if (this.livro.id) {
      // Se o livro já possui um ID, é uma atualização
      this.updateLivro(this.livro);
    } else {
      // Caso contrário, é uma criação
      this.createLivro(this.livro);
    }
  }

  createLivro(livro: Livro) {
    this.livroService.create(Path.LOCALHOST + '/livro', livro).subscribe(success => {
        const successMessage: string = 'Livro ' + livro.titulo +  ' cadastrado com sucesso!';
        this.redirectWithSuccessMessage('pagina-livros', successMessage);
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o livro!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      },
      () => {
      }
    );
  }

  updateLivro(livro: Livro) {
    this.livroService.update(Path.LOCALHOST + '/livro/update', livro.id, livro).subscribe((data: any) => {
      const sucessMessage: string = 'Livro ' + livro.titulo + ' editado com sucesso!';
      this.redirectWithSuccessMessage('pagina-livros', sucessMessage);
    },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível atualizar o livro!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      }
      );
  }


  redirectWithSuccessMessage(route: string, successMessage: string) {
    const navigationExtras: NavigationExtras = {
      state: { message: successMessage },
    };
    const url = this.router.createUrlTree([route], navigationExtras).toString();
    this.router.navigate([url], navigationExtras);
  }



}

