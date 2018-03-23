FROM node:8.1.4-alpine as builder

COPY package.json package-lock.json ./

RUN npm i && mkdir /ng-app && cp -R ./node_modules ./ng-app

WORKDIR /ng-app

COPY . .

RUN $(npm bin)/ng build --prod  --build-optimizer