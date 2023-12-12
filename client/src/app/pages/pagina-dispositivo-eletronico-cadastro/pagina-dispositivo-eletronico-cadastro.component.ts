import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MaterialEsportivo} from "../../models/MaterialEsportivo";
import {CrudService} from "../../services/CrudService";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";
import {DispositivoEletronico} from "../../models/DispositivoEletronico";

@Component({
  selector: 'app-pagina-dispositivo-eletronico-cadastro',
  templateUrl: './pagina-dispositivo-eletronico-cadastro.component.html',
  styleUrls: ['./pagina-dispositivo-eletronico-cadastro.component.css']
})
export class PaginaDispositivoEletronicoCadastroComponent implements OnInit{
  private destroy$ = new Subject();

  // @ts-ignore
  public dispositivoEletronicoForm: FormGroup;
  errorMessage: string = '';
  dispositivoEletronico: DispositivoEletronico;

  constructor(
    private dispositivoEletronicoService: CrudService<DispositivoEletronico>,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient,
    private messageService: MessageService,
  ) {
    this.dispositivoEletronico = new DispositivoEletronico();
    this.initForm();
  }

  initForm(): void{
    this.dispositivoEletronicoForm = this.fb.group({
      id: [null],
      nomeProduto: ['', Validators.required],
      modelo: ['', Validators.required],
      serialNumber: ['', Validators.required],
      marca: ['', Validators.required],
      quantidade: ['', Validators.required],
      quantidadeDisponivel: ['', Validators.required],
      valor: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const state = history.state; // Obtém o estado da rota
    if (state && state.dispositivoEletronico) {
      const dispositivoEletronico: DispositivoEletronico = state.dispositivoEletronico;
      const id = dispositivoEletronico.id;
      if (id) {
        this.handleDispositivoEletronico(dispositivoEletronico);
      }
    }
  }

  updateForm(dispositivoEletronico: DispositivoEletronico) {
    this.dispositivoEletronicoForm.patchValue({
      id: dispositivoEletronico.id,
      nomeProduto: dispositivoEletronico.nomeProduto,
      marca: dispositivoEletronico.marca,
      quantidade: dispositivoEletronico.quantidade,
      quantidadeDisponivel: dispositivoEletronico.quantidadeDisponivel,
      valor: dispositivoEletronico.valor,
      modelo: dispositivoEletronico.modelo,
      serialNumber: dispositivoEletronico.serialNumber
    });

  }

  private handleDispositivoEletronico(dispositivoEletronico: DispositivoEletronico) {
    if (dispositivoEletronico) {
      this.dispositivoEletronico = dispositivoEletronico;
      this.updateForm(dispositivoEletronico);
    }
  }

  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.dispositivoEletronicoForm.markAllAsTouched();
  }

  submit() {
    if (!this.dispositivoEletronicoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.dispositivoEletronicoForm.value;
    this.dispositivoEletronico = new DispositivoEletronico(formValues);

    this.submitLivro();
    return;
  }

  submitLivro() {
    if (!this.dispositivoEletronicoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.dispositivoEletronicoForm.value;
    this.dispositivoEletronico = new DispositivoEletronico(formValues);
    if (this.dispositivoEletronico.id) {
      // Se o dispositivoEletronico já possui um ID, é uma atualização
      this.updateLivro(this.dispositivoEletronico);
    } else {
      // Caso contrário, é uma criação
      this.createLivro(this.dispositivoEletronico);
    }
  }

  createLivro(dispositivoEletronico: DispositivoEletronico) {
    this.dispositivoEletronicoService.create(Path.LOCALHOST + '/dispositivo-eletronico', dispositivoEletronico).subscribe(success => {
        const successMessage: string = 'Dispositivo Eletrônico ' + dispositivoEletronico.nomeProduto +  ' cadastrado com sucesso!';
        this.redirectWithSuccessMessage('pagina-dispositivo-eletronico', successMessage);
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o Dispositivo Eletrônico!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      },
      () => {
      }
    );
  }

  updateLivro(dispositivoEletronico: DispositivoEletronico) {
    this.dispositivoEletronicoService.update(Path.LOCALHOST + '/dispositivo-eletronico/update', dispositivoEletronico.id, dispositivoEletronico).subscribe((data: any) => {
        const sucessMessage: string = 'Dispositivo Eletrônico ' + dispositivoEletronico.nomeProduto + ' editado com sucesso!';
        this.redirectWithSuccessMessage('pagina-dispositivo-eletronico', sucessMessage);
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível atualizar o Dispositivo Eletrônico!";
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
