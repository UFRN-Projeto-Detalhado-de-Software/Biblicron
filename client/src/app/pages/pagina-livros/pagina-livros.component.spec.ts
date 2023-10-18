import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaLivrosComponent } from './pagina-livros.component';

describe('PaginaLivrosComponent', () => {
  let component: PaginaLivrosComponent;
  let fixture: ComponentFixture<PaginaLivrosComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaLivrosComponent]
    });
    fixture = TestBed.createComponent(PaginaLivrosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
