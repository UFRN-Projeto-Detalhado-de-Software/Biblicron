import {Entidade} from "./Entidade";

export class Emprestimo implements Entidade{
  id: number = 0;
  nomeLivro: string = '';
  nomeUsuario: string = '';

  constructor(value: Object = {}) {
    Object.assign(this, value)
  }
}
