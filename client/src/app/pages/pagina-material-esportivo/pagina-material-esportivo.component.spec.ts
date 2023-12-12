import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaMaterialEsportivoComponent } from './pagina-material-esportivo.component';

describe('PaginaMaterialEsportivoComponent', () => {
  let component: PaginaMaterialEsportivoComponent;
  let fixture: ComponentFixture<PaginaMaterialEsportivoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaMaterialEsportivoComponent]
    });
    fixture = TestBed.createComponent(PaginaMaterialEsportivoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
