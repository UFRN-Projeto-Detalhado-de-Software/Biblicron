import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaUsuariosCadastroComponent } from './pagina-usuarios-cadastro.component';

describe('PaginaUsuariosCadastroComponent', () => {
  let component: PaginaUsuariosCadastroComponent;
  let fixture: ComponentFixture<PaginaUsuariosCadastroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaUsuariosCadastroComponent]
    });
    fixture = TestBed.createComponent(PaginaUsuariosCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
