import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaEmprestimosCadastroComponent } from './pagina-emprestimos-cadastro.component';

describe('PaginaEmprestimosCadastroComponent', () => {
  let component: PaginaEmprestimosCadastroComponent;
  let fixture: ComponentFixture<PaginaEmprestimosCadastroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaEmprestimosCadastroComponent]
    });
    fixture = TestBed.createComponent(PaginaEmprestimosCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
