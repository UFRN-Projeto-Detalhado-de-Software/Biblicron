import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaLivrosCadastroComponent } from './pagina-livros-cadastro.component';

describe('PaginaLivrosCadastroComponent', () => {
  let component: PaginaLivrosCadastroComponent;
  let fixture: ComponentFixture<PaginaLivrosCadastroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaLivrosCadastroComponent]
    });
    fixture = TestBed.createComponent(PaginaLivrosCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
