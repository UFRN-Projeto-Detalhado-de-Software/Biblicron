import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PaginaInicialComponent} from "./pages/pagina-inicial/pagina-inicial.component";
import {PaginaLivrosComponent} from "./pages/pagina-livros/pagina-livros.component";
import {PaginaEmprestimosComponent} from "./pages/pagina-emprestimos/pagina-emprestimos.component";
import {PaginaUsuariosComponent} from "./pages/pagina-usuarios/pagina-usuarios.component";
import {PaginaLivrosCadastroComponent} from "./pages/pagina-livros-cadastro/pagina-livros-cadastro.component";
import {
  PaginaEmprestimosCadastroComponent
} from "./pages/pagina-emprestimos-cadastro/pagina-emprestimos-cadastro.component";
import {PaginaUsuariosCadastroComponent} from "./pages/pagina-usuarios-cadastro/pagina-usuarios-cadastro.component";
import {PaginaLoginComponent} from "./pages/pagina-login/pagina-login.component";
import {PaginaEstipularPaginasComponent} from "./pages/pagina-estipular-paginas/pagina-estipular-paginas.component";
import {PaginaEstipularDiasComponent} from "./pages/pagina-estipular-dias/pagina-estipular-dias.component";

const routes: Routes = [
  { path: 'pagina-inicial', component: PaginaInicialComponent },
  { path: 'pagina-livros', component: PaginaLivrosComponent },
  { path: 'pagina-emprestimos', component: PaginaEmprestimosComponent },
  { path: 'pagina-usuarios', component: PaginaUsuariosComponent },
  { path: 'pagina-livros-cadastro', component: PaginaLivrosCadastroComponent },
  { path: 'pagina-livros-cadastro/:id', component: PaginaLivrosCadastroComponent },
  { path: 'pagina-emprestimos-cadastro', component: PaginaEmprestimosCadastroComponent },
  { path: 'pagina-usuarios-cadastro', component: PaginaUsuariosCadastroComponent },
  { path: 'pagina-login', component: PaginaLoginComponent },
  { path: 'pagina-estipular-paginas', component: PaginaEstipularPaginasComponent },
  { path: 'pagina-estipular-dias', component: PaginaEstipularDiasComponent },
  { path: '**', redirectTo: 'pagina-login' },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
