import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {User, UserType} from "../../models/User";
import {Path} from "../../utilities/Path";

@Component({
  selector: 'app-pagina-usuarios-cadastro',
  templateUrl: './pagina-usuarios-cadastro.component.html',
  styleUrls: ['./pagina-usuarios-cadastro.component.css'],
  providers: [CrudService]
})
export class PaginaUsuariosCadastroComponent implements OnInit{
  private destroy$ = new Subject();
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
  ) {
    this.user = new User();
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      userType: [],
      email: ['', Validators.required]
    });
  }


  ngOnInit(): void {

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

  private handleLivro(user: User) {
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
      // Se o livro já possui um ID, é uma atualização
      this.updateUser(this.user);
    } else {
      // Caso contrário, é uma criação
      this.createUser(this.user);
    }
  }

  createUser(user: User) {
    this.userService.create(Path.LOCALHOST + '/user', user).subscribe((data: any) => {
      this.userForm.reset(); // Limpa o formulário
      this.redirectWithSuccessMessage('pagina-usuarios', 'Usuario cadastrado com sucesso!');
    });
  }

  updateUser(user: User) {
    this.userService.update(Path.LOCALHOST + '/user', user.id, user).subscribe((data: any) => {
      this.userForm.reset(); // Limpa o formulário
      this.redirectWithSuccessMessage('pagina-usuarios', 'Usuario editado com sucesso!');
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

