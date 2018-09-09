#!/bin/bash

mv web/client/Dockerfile existing-angular/Dockerfile
rm -R web/client

sudo npm install -g @angular/cli
cd web; ng new client; cd ../

sudo rm -R existing-angular/*