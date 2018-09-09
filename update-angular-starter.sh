#!/bin/bash

mv web/client/Dockerfile existing-angular/Dockerfile
rm -R web/client

sudo npm install -g @angular/cli
cd web; ng new client
cd client; sudo rm -R .git; cd ../../

cp -R existing-angular/Dockerfile web/client/Dockerfile

sudo rm -R existing-angular/*