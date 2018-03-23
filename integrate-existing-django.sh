#!/usr/bin/env bash

#Copiamos el proyecto existente a la carpeta server para el uso de docker
mv web/server/Dockerfile existing-django/*/
mv web/server/entrypoint.sh existing-django/*/
sudo rm -R web/server/*
cp -R existing-django/*/* web/server

#Eliminamos el proyecto angular al ser integrado
sudo rm -R existing-django/*

#Obtenemos la ubicacion del directorio que contiene el settings.py de django
settings_directory=$(dirname "$(find web/server -name "settings.py")")

#Cambiamos el nombre del proyecto django por el nombre usado por docker.
mv $settings_directory web/server/server

#Reemplazamos valores de configuracion con un settings adicional.
mkdir web/server/server/settings
touch web/server/server/settings/__init__.py
cp web/server/server/settings.py web/server/server/settings
sudo rm web/server/server/settings.py
mv web/server/server/settings/settings.py web/server/server/settings/base.py
echo """
from .base import * 

SECRET_KEY = os.environ.get('SECRET_KEY', '')

DEBUG = False if os.environ.get('DEBUG', 'true') == 'false' else True

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOST').split(',')

CORS_ORIGIN_WHITELIST = tuple(os.environ.get('ALLOWED_HOST_REST').split(","))

if 'corsheaders' not in INSTALLED_APPS:
    INSTALLED_APPS.append('corsheaders')
if 'django_celery_results' not in INSTALLED_APPS:
    INSTALLED_APPS.append('django_celery_results')
if 'django_celery_beat' not in INSTALLED_APPS:
    INSTALLED_APPS.append('django_celery_beat')

if 'corsheaders.middleware.CorsMiddleware' not in MIDDLEWARE:
    MIDDLEWARE.append('corsheaders.middleware.CorsMiddleware')

CELERY_BROKER_URL = 'amqp://rabbitmq:aq123d@rabbitmq//'
CELERY_RESULT_BACKEND = 'django-db'

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': os.environ.get('DB_NAME', ''),
        'USER': os.environ.get('DB_USER', ''),
        'PASSWORD': os.environ.get('DB_PASS', ''),
        'HOST': os.environ.get('DB_HOST', ''),
        'PORT': 5432,
    }
}

STATIC_URL = '/static/'
STATIC_ROOT = 'static/'

MEDIA_ROOT = 'media/'
MEDIA_URL = '/media/'

ROOT_URLCONF = 'server.urls'

WSGI_APPLICATION = 'server.wsgi.application'

""" >> web/server/server/settings/production.py

echo """
from .base import * 

SECRET_KEY = os.environ.get('SECRET_KEY', '')

DEBUG = False if os.environ.get('DEBUG', 'true') == 'false' else True

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOST').split(',')

CORS_ORIGIN_WHITELIST = tuple(os.environ.get('ALLOWED_HOST_REST').split(","))

if 'corsheaders' not in INSTALLED_APPS:
    INSTALLED_APPS.append('corsheaders')
if 'django_celery_results' not in INSTALLED_APPS:
    INSTALLED_APPS.append('django_celery_results')
if 'django_celery_beat' not in INSTALLED_APPS:
    INSTALLED_APPS.append('django_celery_beat')

if 'corsheaders.middleware.CorsMiddleware' not in MIDDLEWARE:
    MIDDLEWARE.append('corsheaders.middleware.CorsMiddleware')

CELERY_BROKER_URL = 'amqp://rabbitmq:aq123d@rabbitmq//'
CELERY_RESULT_BACKEND = 'django-db'
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': os.environ.get('DB_NAME', ''),
        'USER': os.environ.get('DB_USER', ''),
        'PASSWORD': os.environ.get('DB_PASS', ''),
        'HOST': os.environ.get('DB_HOST', ''),
        'PORT': 5432,
    }
}

STATIC_URL = '/static/'
STATICFILES_DIRS = [
    '/home/app/server/static',
]

MEDIA_ROOT = 'media/'
MEDIA_URL = '/media/'

ROOT_URLCONF = 'server.urls'

WSGI_APPLICATION = 'server.wsgi.application'

""" >> web/server/server/settings/development.py

#Añadidos librerias obligatorias al proyecto django existente, si ya exisen se actualizan.
requirements=$(cat web/server/requirements.txt | sed -r '/gunicorn==(\w.+)/g' | sed -r '/psycopg2==(\w.+)/g' | sed -r '/psycopg2-binary==(\w.+)/g' | sed -r '/django-cors-headers==(\w.+)/g')
echo $requirements | tr " " "\n" > web/server/requirements.txt
echo """
gunicorn==19.7.1
psycopg2-binary==2.7.4
django-cors-headers==2.2
""" >> web/server/requirements.txt