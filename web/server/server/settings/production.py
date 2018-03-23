from .base import * 

CORS_ORIGIN_WHITELIST = tuple(os.environ.get('ALLOWED_HOST_REST').split(","))

INSTALLED_APPS.append('corsheaders')
INSTALLED_APPS.append('django_celery_results')
INSTALLED_APPS.append('django_celery_beat')

MIDDLEWARE.append('corsheaders.middleware.CorsMiddleware')

CELERY_BROKER_URL = 'amqp://rabbitmq:aq123d@rabbitmq//'
CELERY_RESULT_BACKEND = 'django-db'

STATIC_URL = '/static/'
STATIC_ROOT = 'static/'