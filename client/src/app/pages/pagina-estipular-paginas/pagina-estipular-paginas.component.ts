import { Component } from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User, UserType} from "../../models/User";
import {CrudService} from "../../services/CrudService";
import {NavigationExtras, Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";
import {Estipular} from "../../models/Estipular";

@Component({
  selector: 'app-pagina-estipular-paginas',
  templateUrl: './pagina-estipular-paginas.component.html',
  styleUrls: ['./pagina-estipular-paginas.component.css']
})
export class PaginaEstipularPaginasComponent {
  private destroy$ = new Subject();
  // @ts-ignore
  public estipularForm: FormGroup;
  errorMessage: string = '';
  estipular: Estipular;

  constructor(
    private estipularService: CrudService<Estipular>,
    private router: Router,
    private fb: FormBuilder,
    private messageService: MessageService,
  ) {
    this.estipular = new Estipular();
    this.initForm();
  }


  ngOnInit(): void {
  }

  initForm(): void{
    this.estipularForm = this.fb.group({
      id: [null],
      nomeLivro: ['', Validators.required],
      parametro: [Validators.required],
    });
  }


  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.estipularForm.markAllAsTouched();
  }

  submit() {
    if (!this.estipularForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.estipularForm.value;
    this.estipular = new Estipular(formValues);

    this.submitEstipular(this.estipular);
    return;
  }

  submitEstipular(estipular: Estipular) {
    this.estipularService.create(Path.LOCALHOST + '/livro/estipularPrazoPaginas', estipular).subscribe(sucess => {
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível estipular o prazo!";
        }

        this.messageService.add({ severity: 'info', summary: 'Info', detail: errorMessage });
      }
    );
  }

}
