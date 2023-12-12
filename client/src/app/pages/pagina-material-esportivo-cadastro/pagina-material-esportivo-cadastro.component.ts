import {Component, OnInit} from '@angular/core';
import {Subject} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CrudService} from "../../services/CrudService";
import {ActivatedRoute, NavigationExtras, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {MessageService} from "primeng/api";
import {Path} from "../../utilities/Path";
import {MaterialEsportivo} from "../../models/MaterialEsportivo";

@Component({
  selector: 'app-pagina-material-esportivo-cadastro',
  templateUrl: './pagina-material-esportivo-cadastro.component.html',
  styleUrls: ['./pagina-material-esportivo-cadastro.component.css']
})
export class PaginaMaterialEsportivoCadastroComponent implements OnInit{
  private destroy$ = new Subject();

  // @ts-ignore
  public materialEsportivoForm: FormGroup;
  errorMessage: string = '';
  materialEsportivo: MaterialEsportivo;
  tamanhos: any[] = [
    { name: 'PP', value: 'PP' },
    { name: 'P', value: 'P' },
    { name: 'M', value: 'M' },
    { name: 'G', value: 'G' },
    { name: 'GG', value: 'GG' },
  ];

  categorias: any[] = [
    { name: 'FUTEBOL', value: 'FUTEBOL'},
    { name: 'NATACAO', value: 'NATACAO'},
    { name: 'TENIS', value: 'TENIS'},
    { name: 'CORRIDA', value: 'CORRIDA'},
    { name: 'MUSCULACAO', value: 'MUSCULACAO'},
    { name: 'VOLEI', value: 'VOLEI'},
  ]

  constructor(
    private materialEsportivoService: CrudService<MaterialEsportivo>,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient,
    private messageService: MessageService,
  ) {
    this.materialEsportivo = new MaterialEsportivo();
    this.initForm();
  }

  initForm(): void{
    this.materialEsportivoForm = this.fb.group({
      id: [null],
      nomeProduto: ['', Validators.required],
      marca: ['', Validators.required],
      quantidade: ['', Validators.required],
      quantidadeDisponivel: ['', Validators.required],
      valor: ['', Validators.required],
      tamanhos: [[]],
      categoria: [null],
    });
  }

  ngOnInit(): void {
    const state = history.state; // Obtém o estado da rota
    if (state && state.materialEsportivo) {
      const materialEsportivo: MaterialEsportivo = state.materialEsportivo;
      const id = materialEsportivo.id;
      if (id) {
        this.handleMaterialEsportivo(materialEsportivo);
      }
    }
  }

  updateForm(materialEsportivo: MaterialEsportivo) {
    this.materialEsportivoForm.patchValue({
      id: materialEsportivo.id,
      nomeProduto: materialEsportivo.nomeProduto,
      marca: materialEsportivo.marca,
      quantidade: materialEsportivo.quantidade,
      quantidadeDisponivel: materialEsportivo.quantidadeDisponivel,
      valor: materialEsportivo.valor,
      tamanhos: materialEsportivo.tamanhos,
      categoria: materialEsportivo.categoria
    });

  }

  private handleMaterialEsportivo(materialEsportivo: MaterialEsportivo) {
    if (materialEsportivo) {
      this.materialEsportivo = materialEsportivo;
      this.updateForm(materialEsportivo);
    }
  }

  private markAllFieldsAsTouchedAndShowErrorMessage(): void {
    this.materialEsportivoForm.markAllAsTouched();
  }

  submit() {
    if (!this.materialEsportivoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.materialEsportivoForm.value;
    this.materialEsportivo = new MaterialEsportivo(formValues);

    this.submitLivro();
    return;
  }

  submitLivro() {
    if (!this.materialEsportivoForm.valid) {
      this.markAllFieldsAsTouchedAndShowErrorMessage();
      return;
    }

    const formValues = this.materialEsportivoForm.value;
    this.materialEsportivo = new MaterialEsportivo(formValues);
    if (this.materialEsportivo.id) {
      // Se o materialEsportivo já possui um ID, é uma atualização
      this.updateLivro(this.materialEsportivo);
    } else {
      // Caso contrário, é uma criação
      this.createLivro(this.materialEsportivo);
    }
  }

  createLivro(materialEsportivo: MaterialEsportivo) {
    this.materialEsportivoService.create(Path.LOCALHOST + '/material-esportivo', materialEsportivo).subscribe(success => {
        const successMessage: string = 'Material Esportivo ' + materialEsportivo.nomeProduto +  ' cadastrado com sucesso!';
        this.redirectWithSuccessMessage('pagina-material-esportivo', successMessage);
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível cadastrar o Material Esportivo!";
        }

        this.messageService.add({ severity: 'error', summary: 'Erro', detail: errorMessage });
      },
      () => {
      }
    );
  }

  updateLivro(materialEsportivo: MaterialEsportivo) {
    this.materialEsportivoService.update(Path.LOCALHOST + '/material-esportivo/update', materialEsportivo.id, materialEsportivo).subscribe((data: any) => {
        const sucessMessage: string = 'Material Esportivo ' + materialEsportivo.nomeProduto + ' editado com sucesso!';
        this.redirectWithSuccessMessage('pagina-material-esportivo', sucessMessage);
      },
      error => {
        let errorMessage: string;

        if (error.error) {
          errorMessage = JSON.stringify(error.error); // Converte o objeto de resposta em uma string
        } else {
          errorMessage = "Não foi possível atualizar o Material Esportivo!";
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
