#!/bin/bash

#Backups files important!
cp web/server/Dockerfile existing-django/Dockerfile
cp web/server/entrypoint.sh existing-django/entrypoint.sh
cp web/server/wait-for-it.sh existing-django/wait-for-it.sh
sudo rm -R web/server

#Install virtualenv and install django, gunicorn
sudo pip install virtualenv
virtualenv -p python3 djangoenv
source djangoenv/bin/activate
pip install django gunicorn

#Create project django and copy backups files important!
cd web; django-admin startproject server
cd server; pip freeze > requirements.txt; cd ../../

cp existing-django/Dockerfile web/server/Dockerfile
cp existing-django/entrypoint.sh web/server/entrypoint.sh
cp existing-django/wait-for-it.sh web/server/wait-for-it.sh
sudo rm -R existing-django/*

#Split django configuration into states
mkdir web/server/server/settings
touch web/server/server/settings/__init__.py
cp web/server/server/settings.py web/server/server/settings/base.py
sudo rm web/server/server/settings.py

#Creation basic config developer and production
echo """
from .base import * 

DEBUG = False if os.environ.get('DEBUG', 'true') == 'false' else True

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOST').split(',')

STATICFILES_DIRS = [
    '/home/app/server/static',
]

MEDIA_ROOT = 'media/'
MEDIA_URL = '/media/'
""" >> web/server/server/settings/development.py
echo """
from .base import * 

DEBUG = False if os.environ.get('DEBUG', 'true') == 'false' else True

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOST').split(',')

STATIC_ROOT = 'static/'

MEDIA_ROOT = 'media/'
MEDIA_URL = '/media/'
""" >> web/server/server/settings/production.py

mkdir web/server/media; touch web/server/media/.gitkeep
mkdir web/server/static; touch web/server/static/.gitkeep

echo "**************************************"
while true; do
read -p 'ADD DATABASE (postgresql or mysql): ' db

case $db in
	"postgresql")
	    echo "install psycopg2..."
	    pip install psycopg2
	    echo "config databases in settings..."
databases="""
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
"""
	    echo "${databases}" >> web/server/server/settings/development.py
		echo "${databases}" >> web/server/server/settings/production.py
		cd web; cd server; pip freeze > requirements.txt; cd ../../
	    break;
	;;
	"mysql")
	    echo "install mysqlclient..."
	    sudo apt-get install libmysqlclient-dev
	    pip install mysqlclient
	    echo "config databases in settings..."
databases="""
#DATABASE
DATABASES = {
	'default': {
		'ENGINE': 'django.db.backends.mysql',
		'NAME': os.environ.get('DB_NAME', ''),
		'USER': os.environ.get('DB_USER', ''),
		'PASSWORD': os.environ.get('DB_PASS', ''),
		'HOST': os.environ.get('DB_HOST', ''),
		'PORT': 3306,
	}
}
"""
	    echo "${databases}" >> web/server/server/settings/development.py
		echo "${databases}" >> web/server/server/settings/production.py
		cd web; cd server; pip freeze > requirements.txt; cd ../../
	    break;
	;;
esac
done
echo "**************************************"
while true; do
read -p 'ADD DJANGO REST FRAMEWORK (y/N): ' drf

case $drf in
	[yY][eE][sS]|[yY])
	    echo "install djangorestframework..."
	    pip install djangorestframework
	    pip install django-cors-headers
	    echo "add 'rest_framework' and 'corsheaders' to your INSTALLED_APPS setting..."
restframework="""
#DJANGO REST FRAMEWORK
CORS_ORIGIN_WHITELIST = tuple(os.environ.get('ALLOWED_HOST_REST').split(','))
INSTALLED_APPS.append('corsheaders')
INSTALLED_APPS.append('rest_framework')
MIDDLEWARE.append('corsheaders.middleware.CorsMiddleware')
"""
	    echo "${restframework}" >> web/server/server/settings/development.py
		echo "${restframework}" >> web/server/server/settings/production.py	
		cd web; cd server; pip freeze > requirements.txt; cd ../../    
	    break;
	;;
	[nN])
	    break;
	;;
esac
done
echo "**************************************"
while true; do
read -p 'ADD CELERY (y/N): ' drf

case $drf in
	[yY][eE][sS]|[yY])
	    echo "install celery and django-celery-results..."
	    pip install celery
	    pip install django-celery-results
	    echo "add 'django_celery_results' to your INSTALLED_APPS setting..."
celery="""
#CELERY
INSTALLED_APPS.append('django_celery_results')
CELERY_BROKER_URL = 'amqp://rabbitmq:aq123d@rabbitmq//'
CELERY_RESULT_BACKEND = 'django-db'
"""
	    echo "${celery}" >> web/server/server/settings/development.py
		echo "${celery}" >> web/server/server/settings/production.py

celery_init="""
from __future__ import absolute_import, unicode_literals

from .celery import app as celery_app

__all__ = ['celery_app']
"""
	    echo "${celery_init//  /}" >> web/server/server/__init__.py

celery_settings="""
from __future__ import absolute_import, unicode_literals
import os
from celery import Celery

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'server.settings')

app = Celery('server')

app.config_from_object('django.conf:settings', namespace='CELERY')

app.autodiscover_tasks()

@app.task(bind=True)
def debug_task(self):
    print('Request: {0!r}'.format(self.request))
"""
	    echo "${celery_settings}" >> web/server/server/celery.py   
	    cd web; cd server; pip freeze > requirements.txt; cd ../../
	    break;
	;;
	[nN])
	    break;
	;;
esac
done
echo "**************************************"
while true; do
read -p 'ADD CELERY BEAT (y/N): ' drf

case $drf in
	[yY][eE][sS]|[yY])
	    echo "install django-celery-beat..."
	    pip install django-celery-beat
	    echo "creating dockerignore..."
		echo "celerybeat.pid" >> web/server/.dockerignore
		cd web; cd server; pip freeze > requirements.txt; cd ../../
	    break;
	;;
	[nN])
	    break;
	;;
esac
done

sudo rm -R djangoenv