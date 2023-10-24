import { Component } from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Estipular} from "../../models/Estipular";
import {CrudService} from "../../services/CrudService";
import {Router} from "@angular/router";
import {MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";

@Component({
  selector: 'app-pagina-estipular-dias',
  templateUrl: './pagina-estipular-dias.component.html',
  styleUrls: ['./pagina-estipular-dias.component.css']
})
export class PaginaEstipularDiasComponent {
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
    this.estipularService.create(Path.LOCALHOST + '/livro/estipularPrazoDia', estipular).subscribe(sucess => {
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
