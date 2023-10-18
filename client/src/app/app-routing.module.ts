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

const routes: Routes = [
  { path: 'pagina-inicial', component: PaginaInicialComponent },
  { path: 'pagina-livros', component: PaginaLivrosComponent },
  { path: 'pagina-emprestimos', component: PaginaEmprestimosComponent },
  { path: 'pagina-usuarios', component: PaginaUsuariosComponent },
  { path: 'pagina-livros-cadastro', component: PaginaLivrosCadastroComponent },
  { path: 'pagina-emprestimos-cadastro', component: PaginaEmprestimosCadastroComponent },
  { path: 'pagina-usuarios-cadastro', component: PaginaUsuariosCadastroComponent },
  { path: '**', redirectTo: 'pagina-inicial' },
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
