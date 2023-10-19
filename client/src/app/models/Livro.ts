import {Entidade} from "./Entidade";

export class Livro implements Entidade{
  id: number = 0;
  titulo: string = '';
  autor: string = '';
  quantidade: number = 0;
  quantidadeDisponivel: number = 0;
  dataPublicacao: Date = new Date();
  paginas: number = 0;
  genero: Genero[] = [];


  constructor(value: Object = {}) {
    Object.assign(this, value);
  }

}

export enum Genero {
 FICCAO_CIENTIFICA = "Ficção Científica",
  FANTASIA = "Fantasia",
  ROMANCE = "Romance",
  SUSPENSE = "Suspense",
  AVENTURA = "Aventura",
  BIOGRAFIA = "Biografia",
  DIDATICOS = "Didáticos",
  HISTORIA_EM_QUADRINHOS = "História em Quadrinhos",
  CLASSICOS = "Clássicos"
}
