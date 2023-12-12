import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaMaterialEsportivoCadastroComponent } from './pagina-material-esportivo-cadastro.component';

describe('PaginaMaterialEsportivoCadastroComponent', () => {
  let component: PaginaMaterialEsportivoCadastroComponent;
  let fixture: ComponentFixture<PaginaMaterialEsportivoCadastroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaMaterialEsportivoCadastroComponent]
    });
    fixture = TestBed.createComponent(PaginaMaterialEsportivoCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
