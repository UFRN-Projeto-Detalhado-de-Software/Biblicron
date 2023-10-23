import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Emprestimo} from "../../models/Emprestimo";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {Path} from "../../utilities/Path";
import {User} from "../../models/User";
import {MessageService} from "primeng/api";

@Component({
  selector: 'app-pagina-login',
  templateUrl: './pagina-login.component.html',
  styleUrls: ['./pagina-login.component.css']
})
export class PaginaLoginComponent implements OnInit{
  private destroy$ = new Subject();
  public userForm: FormGroup;
  errorMessage: string = '';
  user: User;

  constructor(
    private userService: CrudService<User>,
    private router: Router,
    private fb: FormBuilder,
    private messageService: MessageService,
  ) {
    this.user = new User();
    this.userForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }


  ngOnInit(): void {
    localStorage.removeItem('currentUser'); //limpa as informações do usuário
  }

  updateForm(user: User) {
    this.userForm.patchValue({
      id: user.id,
      username: user.username,
      password: user.password,
    });

  }

  private handleEmprestimo(user: User) {
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

    this.submitEmprestimo();
    return;
  }

  submitEmprestimo() {
    if (!this.userForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }
    this.login(this.user);
  }

  login(user: User) {
    this.userService.login(Path.LOCALHOST + '/user/login', user).subscribe((data: any) => {
      const successMessage: string = 'Login realizado com sucesso!';
      localStorage.setItem('currentUser', JSON.stringify(data));
      this.redirectWithSuccessMessage('pagina-inicial', successMessage);
    },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error);
        } else {
          errorMessage = "Não foi possível realizar o login!";
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
