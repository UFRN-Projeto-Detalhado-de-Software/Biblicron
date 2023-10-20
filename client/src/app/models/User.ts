import {Entidade} from "./Entidade";

export class User implements Entidade{
  id: number = 0;
  username: string = '';
  password: string = '';
  userType: UserType = UserType.COMMON;
  email: string = '';

  constructor(value: Object = {}) {
    Object.assign(this, value);
  }
}

export enum UserType{
  COMMON = "COMMON",
  ADMIN = "ADMIN",
  MANAGER = "MANAGER"
}
