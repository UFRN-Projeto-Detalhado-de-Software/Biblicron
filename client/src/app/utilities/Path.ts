export class Path {

  public static readonly AUTH_LOGIN = new Path('auth/login', 'Login', 'core-auth-service');
  public static readonly ERROR_UNAUTHORIZED = new Path('error/404', '401', 'core-auth-service');
  public static readonly ERROR_NOTFOUND = new Path('error/404', '404', 'core-auth-service');

  public static readonly LOCALHOST = new Path('localhost:4200/', 'Api Local Host');

  public constructor(
    public readonly caminho: string,
    public readonly nomeExibicao: string,
    public readonly nomeService?: string
  ) {
  }

}
