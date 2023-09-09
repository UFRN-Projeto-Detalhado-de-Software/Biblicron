FROM node:lts-alpine3.17

COPY . /client

WORKDIR /client

RUN npm install -g npm@10.1.0

RUN npm install

RUN npm install -g @angular/cli@latest

CMD ng serve --host 0.0.0.0
