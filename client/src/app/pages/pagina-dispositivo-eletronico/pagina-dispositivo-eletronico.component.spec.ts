import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaDispositivoEletronicoComponent } from './pagina-dispositivo-eletronico.component';

describe('PaginaDispositivoEletronicoComponent', () => {
  let component: PaginaDispositivoEletronicoComponent;
  let fixture: ComponentFixture<PaginaDispositivoEletronicoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaDispositivoEletronicoComponent]
    });
    fixture = TestBed.createComponent(PaginaDispositivoEletronicoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
