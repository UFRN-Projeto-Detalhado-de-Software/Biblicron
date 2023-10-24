import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaEstipularPaginasComponent } from './pagina-estipular-paginas.component';

describe('PaginaEstipularPaginasComponent', () => {
  let component: PaginaEstipularPaginasComponent;
  let fixture: ComponentFixture<PaginaEstipularPaginasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaEstipularPaginasComponent]
    });
    fixture = TestBed.createComponent(PaginaEstipularPaginasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
