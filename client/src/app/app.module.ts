import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AsideMenuComponent } from './aside/aside-menu/aside-menu.component';
import { AsideComponent } from './aside/aside.component';
import { PaginaInicialComponent } from './pages/pagina-inicial/pagina-inicial.component';
import { PaginaLivrosComponent } from './pages/pagina-livros/pagina-livros.component';
import { PaginaEmprestimosComponent } from './pages/pagina-emprestimos/pagina-emprestimos.component';
import { PaginaUsuariosComponent } from './pages/pagina-usuarios/pagina-usuarios.component';
import { PaginaLivrosCadastroComponent } from './pages/pagina-livros-cadastro/pagina-livros-cadastro.component';
import { PaginaEmprestimosCadastroComponent } from './pages/pagina-emprestimos-cadastro/pagina-emprestimos-cadastro.component';
import { PaginaUsuariosCadastroComponent } from './pages/pagina-usuarios-cadastro/pagina-usuarios-cadastro.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {TableModule} from "primeng/table";

@NgModule({
  declarations: [
    AppComponent,
    AsideMenuComponent,
    AsideComponent,
    PaginaInicialComponent,
    PaginaLivrosComponent,
    PaginaEmprestimosComponent,
    PaginaUsuariosComponent,
    PaginaLivrosCadastroComponent,
    PaginaEmprestimosCadastroComponent,
    PaginaUsuariosCadastroComponent,
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
        TableModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
