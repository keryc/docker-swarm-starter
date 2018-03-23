# Docker Swarm Starter

Este repositorio fue creado con el fin de mejorar el desarrollo de proyectos web profesionales y su despliegue a produccion.

  - La carpeta **swarmpit** contiene una interfaz simple y fácil de usar para su clúster Docker Swarm. Puede administrar sus servicios, volúmenes, redes, etc.
  - La carpeta **web** contiene un conjunto de servicios con dos modalidades de arranque(develepment o production).
      - Contiene una aplicación [Angular] (https://angular.io/), construida con [Angular-Cli]
      - Contiene una aplicación simple [Django] (https://www.djangoproject.com/) incluye gunicorn para prod. 
      - Contiene un servicio nginx para servir la aplicacion angular y la aplicacion django con static y media.
      - Contiene un servicio postgresql para la creacion de base de datos usada por django.
  - La carpeta **letsencrypts** contiente unos servicios que permiten la creación y renovación automática de certificados Let's Encrypt para cualquier contenedor donde se especifiquen ciertas variables.
  - La carpeta **mail** contiente servidor de correo completo con soporte SSL TLS. POP3s, SMTP (s), IMAP, Spamassassin, Roundcube (HTTPS), SPF, DKIM con instalación simple y administración web.
  - La carpeta **existing-django** nos sirve solo en el caso de querer integrar un proyecto django existente al entorno.
  - La carpeta **existing-angular** nos sirve solo en el caso de querer integrar un proyecto angular existente al entorno.

### Tecnología

Docker Swarm Starter utiliza una serie de proyectos de código abierto para funcionar correctamente:

* [Angular.io](https://angular.io/) - framework de desarrollo para JavaScript creado por Google!
* [Angular CLI](https://cli.angular.io/) - es la forma más cómoda para empezar a desarrollar aplicaciones web!
* [Django](https://www.djangoproject.com/)  - framework para aplicaciones web gratuito y open source!
* [Postgresql](https://www.djangoproject.com/)  - framework para aplicaciones web gratuito y open source!
* [Celery](http://www.celeryproject.org/)  - libreria para creacion de tareas en segundo plano en python.
* [RabbitMQ](https://www.rabbitmq.com/)  - permite la gestión de tus propios brokers de mensajería.
* [Nginx](https://nginx.org/en/) - servidor web y proxy inverso, multiplataforma, ligero y de alto rendimiento!
* [Gunicorn](http://gunicorn.org/) - servidor HTTP para sistemas Unix que cumple la especificación WSGI!
* [PM2](http://pm2.keymetrics.io/) - gestor avanzado de procesos de producción para Node.js
* [LetsEncrypts](https://letsencrypt.org/) -  autoridad de certificación que proporciona certificados sslgratuitos para el cifrado de Seguridad!
* [Docker](https://www.docker.com/) - automatiza el despliegue de aplicaciones dentro de contenedores de software!
* [Docker Swarm](https://docs.docker.com/engine/swarm/) - nos permite gestionar clúster de máquinas Docker (o enjambre)!
* [Swarmpit](https://swarmpit.io/) - interfaz simple y fácil de usar para su clúster Docker Swarm.
* [Poste.io](https://poste.io/) - SMTP + IMAP + POP3 + Antispam + Antivirus + Administración web + correo electrónico web!

### Cómo usarlo?

Pronto estare actualizando este documento con una explicacion de los siguientes casos de uso:

- Ejecucion de Django +(or) Angular +(or) Celery +(or) Celery Beat +(or) Postgresql +(or) Rabbitmp +(or) Nodejs omitiendo aquellos servicios que no se utilizaran.
- Integrando un Proyecto Django Angular o NodeJs existente de una forma rapida y facil.
- Ejecucion en Modo Desarrollo para laborar reflejando cambios en tiempo real o ejecucion en Modo Produccion.
- Ejecucion de Swarmpit para gestion de contenedores y configuracion de Docker Registry existente o Docker Hub
- Ejecucion de LetsEncrypts para autogenerar certificados ssl de forma gratuita y con comprobacion de expiracion.
- Ejecucion de servidor de correos, configuracion y administracion del mismo. 


Espero recibir buen feedback y colaboracion de parte de ustedes, cualquier duda escribirme a kdiaz@hobox.org

__________
Si te a sido de gran ayuda este contenido te invito a contribuir con la obra [![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://paypal.me/kerycdiaz/)