import {Entidade} from "./Entidade";

export class Emprestimo implements Entidade{
  id: number = 0;
  valorFinal: number = 0;
  nomeProduto: string = '';
  nomeUsuario: string = '';

  constructor(value: Object = {}) {
    Object.assign(this, value)
  }
}
