import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaEmprestimosComponent } from './pagina-emprestimos.component';

describe('PaginaEmprestimosComponent', () => {
  let component: PaginaEmprestimosComponent;
  let fixture: ComponentFixture<PaginaEmprestimosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaEmprestimosComponent]
    });
    fixture = TestBed.createComponent(PaginaEmprestimosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
