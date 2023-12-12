import {Entidade} from "./Entidade";

export class DispositivoEletronico implements Entidade{
  id: number = 0;
  nomeProduto: string = '';
  quantidade: number = 0;
  quantidadeDisponivel: number = 0;
  valor: number = 0;
  marca: string = '';
  serialNumber: string = '';
  modelo: string = '';

  constructor(value: Object = {}) {
    Object.assign(this, value);
  }


}
