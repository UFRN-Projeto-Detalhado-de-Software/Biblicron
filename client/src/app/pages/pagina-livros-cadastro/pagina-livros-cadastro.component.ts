import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {Genero, Livro} from "../../models/Livro";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-pagina-livros-cadastro',
  templateUrl: './pagina-livros-cadastro.component.html',
  styleUrls: ['./pagina-livros-cadastro.component.css']
})
export class PaginaLivrosCadastroComponent implements OnInit{
  private destroy$ = new Subject();
  livro: Livro;
  generoLivro: any[] = [
    { name: 'Ficção Científica', value: 'Ficção Científica' },
    { name: 'Fantasia', value: 'Fantasia' },
    { name: 'Romance', value: 'Romance' },
    { name: 'Suspense', value: 'Suspense' },
    { name: 'Aventura', value: 'Aventura' },
    { name: 'Biografia', value: 'Biografia' },
    { name: 'Didáticos', value: 'Didáticos' },
    { name: 'História em Quadrinhos', value: 'História em Quadrinhos' },
    { name: 'Clássicos', value: 'Clássicos' }
  ];


  constructor(
    private livroService: CrudService<Livro>,
    private router: Router,
    private messageService: MessageService,
    private fb: FormBuilder,
    public livroForm: FormGroup,
  ) {
    this.livro = new Livro();
  }


  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.livroForm = this.fb.group({
      id: [null],
      titulo: ['', Validators.required],
      autor: ['', Validators.required],
      quantidade: [Validators.required],
      quantidadeDisponivel: [Validators.required],
      dataPublicacao: [Validators.required],
      paginas: [Validators.required],
      genero: [Validators.required],

    });
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
      genero: livro.paginas
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
    this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Formulário inválido!' });
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
    const endpoint = 'localhost:4200/livro'; // Define o endpoint para criar um novo livro
    this.livroService.create(endpoint, livro).subscribe(
      (response: Livro) => {
        this.redirectWithSuccessMessage('pagina-livros', 'Livro cadastrado com sucesso!');
      },
      (error) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível cadastrar o livro!' });
      },
    );
  }

  updateLivro(livro: Livro) {
    const endpoint = `livros/${livro.id}`; // Define o endpoint para atualizar o livro existente
    this.livroService.update(endpoint, livro.id, livro).subscribe(
      (response: Livro) => {
        this.redirectWithSuccessMessage('pagina-livros', 'Livro atualizado com sucesso!');
      },
      (error) => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Não foi possível atualizar o livro!' });
      },
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

