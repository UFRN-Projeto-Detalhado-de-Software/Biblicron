import {Entidade} from "./Entidade";

export class Livro implements Entidade{
  id: number = 0;
  titulo: string = '';
  autor: string = '';
  quantidade: number = 0;
  quantidadeDisponivel: number = 0;
  dataPublicacao: Date = new Date();
  paginas: number = 0;
  generos: Genero[] = [];


  constructor(value: Object = {}) {
    Object.assign(this, value);
  }

}

export enum Genero {
  FICCAO_CIENTIFICA = "FICCAO_CIENTIFICA",
  FANTASIA = "FANTASIA",
  ROMANCE = "ROMANCE",
  SUSPENSE = "SUSPENSE",
  AVENTURA = "AVENTURA",
  BIOGRAFIA = "BIOGRAFIA",
  DIDATICOS = "DIDATICOS",
  HISTORIA_EM_QUADRINHOS = "HISTORIA_EM_QUADRINHOS",
  CLASSICOS = "CLASSICOS"
}
