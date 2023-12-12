import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaginaDispositivoEletronicoCadastroComponent } from './pagina-dispositivo-eletronico-cadastro.component';

describe('PaginaDispositivoEletronicoCadastroComponent', () => {
  let component: PaginaDispositivoEletronicoCadastroComponent;
  let fixture: ComponentFixture<PaginaDispositivoEletronicoCadastroComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaginaDispositivoEletronicoCadastroComponent]
    });
    fixture = TestBed.createComponent(PaginaDispositivoEletronicoCadastroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
