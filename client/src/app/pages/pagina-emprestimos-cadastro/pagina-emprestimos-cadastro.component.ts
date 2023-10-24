import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {User, UserType} from "../../models/User";
import {Path} from "../../utilities/Path";
import {Emprestimo} from "../../models/Emprestimo";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-emprestimos-cadastro',
  templateUrl: './pagina-emprestimos-cadastro.component.html',
  styleUrls: ['./pagina-emprestimos-cadastro.component.css'],
  providers: [CrudService]
})
export class PaginaEmprestimosCadastroComponent implements OnInit{
  private destroy$ = new Subject();
  public emprestimoForm: FormGroup;
  errorMessage: string = '';
  emprestimo: Emprestimo;

  constructor(
    private emprestimoService: CrudService<Emprestimo>,
    private messageService: MessageService,
    private router: Router,
    private fb: FormBuilder,
  ) {
    this.emprestimo = new Emprestimo();
    this.emprestimoForm = this.fb.group({
      nomeLivro: ['', Validators.required],
      nomeUsuario: ['', Validators.required],
    });
  }


  ngOnInit(): void {

  }

  updateForm(emprestimo: Emprestimo) {
    this.emprestimoForm.patchValue({
      id: emprestimo.id,
      nomeLivro: emprestimo.nomeLivro,
      nomeUsuario: emprestimo.nomeUsuario,
    });

  }

  private handleEmprestimo(emprestimo: Emprestimo) {
    if (emprestimo) {
      this.emprestimo = emprestimo;
      this.updateForm(emprestimo);
    }
  }

  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.emprestimoForm.markAllAsTouched();
  }

  submit() {
    if (!this.emprestimoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.emprestimoForm.value;
    this.emprestimo = new Emprestimo(formValues);

    this.submitEmprestimo();
    return;
  }

  submitEmprestimo() {
    if (!this.emprestimoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }
    this.create(this.emprestimo);
  }

  create(emprestimo: Emprestimo) {
    this.emprestimoService.create(Path.LOCALHOST + '/emprestimo/realizar', emprestimo).subscribe((data: any) => {
      const sucessMessage: string = 'Empréstimo de id: ' + data.id + ' cadastrado com sucesso!';
      this.redirectWithSuccessMessage('pagina-emprestimos', sucessMessage);
    },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o empréstimo!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      });
  }


  redirectWithSuccessMessage(route: string, successMessage: string) {
    const navigationExtras: NavigationExtras = {
      state: { message: successMessage },
    };
    const url = this.router.createUrlTree([route], navigationExtras).toString();
    this.router.navigate([url], navigationExtras);
  }

}

