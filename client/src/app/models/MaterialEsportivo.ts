import {Entidade} from "./Entidade";

export class MaterialEsportivo implements Entidade{
  id: number = 0;
  nomeProduto: string = '';
  quantidade: number = 0;
  quantidadeDisponivel: number = 0;
  valor: number = 0;
  marca: string = '';
  categoria: Categoria | null = null;
  tamanhos: Tamanho[] = [];


  constructor(value: Object = {}) {
    Object.assign(this, value);
  }

}

export enum Categoria{
  FUTEBOL = 'FUTEBOL',
  NATACAO = 'NATACAO',
  TENIS = 'TENIS',
  CORRIDA = 'CORRIDA',
  MUSCULACAO = 'MUSCULACAO',
  VOLEI = 'VOLEI'
}

export enum Tamanho {
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
