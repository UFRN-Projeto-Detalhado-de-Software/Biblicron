## Rodar com Docker

Primeiramente, construa a imagem com o comando abaixo:

`$ sudo docker build --tag spring .`.

Após a imagam de nome `spring` estiver criada, execute este comando para subir o container com o mesmo nome da imagem:

`$ sudo docker run --name spring --net=host -p 8081:8081 -v $(pwd):/server -it spring`.

Este `--net=host` é necessário para que o container possa acessar a rede da máquina host e não a própria rede.
Caso contrário, ele não consegueria se conectar com o banco que tá em localhost por exemplo.

### Ciclo de desenvolvimento

Você pode parar o spring dentro container com `Ctrl+C`.

Estando fora do container execute `$ sudo docker start -i spring` e o container spring estará de pé novamente.
