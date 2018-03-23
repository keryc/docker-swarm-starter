#!/usr/bin/env bash

#Copiamos el proyecto existente a la carpeta client para el uso de docker
mv web/client/Dockerfile existing-angular/*/
rm -R web/client/*
cp -R existing-angular/*/* web/client

#Eliminamos el proyecto angular al ser integrado
sudo rm -R existing-angular/*