import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {User, UserType} from "../../models/User";
import {Path} from "../../utilities/Path";
import {Livro} from "../../models/Livro";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-usuarios-cadastro',
  templateUrl: './pagina-usuarios-cadastro.component.html',
  styleUrls: ['./pagina-usuarios-cadastro.component.css'],
  providers: [CrudService]
})
export class PaginaUsuariosCadastroComponent implements OnInit{
  private destroy$ = new Subject();
  // @ts-ignore
  public userForm: FormGroup;
  errorMessage: string = '';
  user: User;
  userTypes: any[] = [
    { name: 'Usuário Comum', value: UserType.COMMON },
    { name: 'Gerente', value: UserType.MANAGER },
    { name: 'Administrador(Super)', value: UserType.ADMIN },
  ];



  constructor(
    private userService: CrudService<User>,
    private router: Router,
    private fb: FormBuilder,
    private messageService: MessageService,
  ) {
    this.user = new User();
    this.initForm();
  }


  ngOnInit(): void {
    const state = history.state; // Obtém o estado da rota
    if (state && state.user) {
      const user: User = state.user;
      const id = user.id;
      if (id) {
        this.handleUser(user);
      }
    }
  }

  initForm(): void{
    this.userForm = this.fb.group({
      id: [null],
      username: ['', Validators.required],
      password: ['', Validators.required],
      userType: [],
      email: ['', Validators.required]
    });
  }

  updateForm(user: User) {
    this.userForm.patchValue({
      id: user.id,
      username: user.username,
      password: user.password,
      userType: user.userType,
      email: user.email,
    });

  }

  private handleUser(user: User) {
    if (user) {
      this.user = user;
      this.updateForm(user);
    }
  }

  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.userForm.markAllAsTouched();
  }

  submit() {
    if (!this.userForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.userForm.value;
    this.user = new User(formValues);

    this.submitUser();
    return;
  }

  submitUser() {
    if (!this.userForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.userForm.value;
    this.user = new User(formValues);
    if (this.user.id) {
      // Se o user já possui um ID, é uma atualização
      this.updateUser(this.user);
    } else {
      // Caso contrário, é uma criação
      this.createUser(this.user);
    }
  }

  createUser(user: User) {
    this.userService.create(Path.LOCALHOST + '/user', user).subscribe(sucess => {
      const sucessMessage: string = 'User ' + user.username + ' cadastrado com sucesso!';
      this.redirectWithSuccessMessage('pagina-usuarios', sucessMessage);
    },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o usuário!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      }
    );
  }

  updateUser(user: User) {
    this.userService.update(Path.LOCALHOST + '/user/update', user.id, user).subscribe(sucess => {
      const sucessMessage: string = 'User ' + user.username + ' editado com sucesso!';
      this.redirectWithSuccessMessage('pagina-usuarios', sucessMessage);
    },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o usuário!";
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

