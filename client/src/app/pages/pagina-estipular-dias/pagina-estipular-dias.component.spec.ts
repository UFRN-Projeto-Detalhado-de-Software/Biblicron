import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaEstipularDiasComponent } from './pagina-estipular-dias.component';

describe('PaginaEstipularDiasComponent', () => {
  let component: PaginaEstipularDiasComponent;
  let fixture: ComponentFixture<PaginaEstipularDiasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaEstipularDiasComponent]
    });
    fixture = TestBed.createComponent(PaginaEstipularDiasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
