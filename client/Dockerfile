FROM node:lts-alpine3.17

COPY . /client

WORKDIR /client

RUN npm install -g @angular/cli

CMD ["ng", "serve", "--open"]

EXPOSE 4200
