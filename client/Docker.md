## Executar com Docker

Primeiro execute `npm install`.

Segungo execute `sudo docker build --tag angular-client`.

Por último, execute `sudo docker run --name angular -p 4200:4200 -v $(pwd):/angular-client -it angular`.
