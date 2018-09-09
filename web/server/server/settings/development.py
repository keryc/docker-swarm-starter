
from .base import * 

DEBUG = False if os.environ.get('DEBUG', 'true') == 'false' else True

ALLOWED_HOSTS = os.environ.get('ALLOWED_HOST').split(',')

STATICFILES_DIRS = [
    '/home/app/server/static',
]

MEDIA_ROOT = 'media/'
MEDIA_URL = '/media/'


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


#DJANGO REST FRAMEWORK
CORS_ORIGIN_WHITELIST = tuple(os.environ.get('ALLOWED_HOST_REST').split(','))
INSTALLED_APPS.append('corsheaders')
INSTALLED_APPS.append('rest_framework')
MIDDLEWARE.append('corsheaders.middleware.CorsMiddleware')


#CELERY
INSTALLED_APPS.append('django_celery_results')
CELERY_BROKER_URL = 'amqp://rabbitmq:aq123d@rabbitmq//'
CELERY_RESULT_BACKEND = 'django-db'

