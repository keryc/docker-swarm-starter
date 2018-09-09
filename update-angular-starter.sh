#!/bin/bash

while true; do
read -p """
CREATE PROJECT:
	* Angular.Io (0)
	* StencilJs (Ionic-Pwa) (1)

	Insert option: """ db 

case $db in
	"0")
		mv web/client/Dockerfile existing-angular/Dockerfile
		rm -R web/client

		sudo npm install -g @angular/cli
		cd web; ng new client
		cd client; sudo rm -R .git; cd ../../

		cp -R existing-angular/Dockerfile web/client/Dockerfile

		sudo rm -R existing-angular/*
	    break;
	;;
	"1")
		rm -R web/client

		cd web; npm init stencil ionic-pwa -y --name client
		cd client; npm install; cd ../../

		dockerfile="""
FROM node:8.1.4-alpine as builder

COPY package.json ./

RUN npm i && mkdir /ng-app && cp -R ./node_modules ./ng-app

WORKDIR /ng-app

COPY . .

RUN $(npm bin)/run build
"""
	    echo "${dockerfile}" >> web/client/Dockerfile

	    break;
	;;
esac
done