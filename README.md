[ Users: ]

administrator: admin / admin
developer: dev / dev
user: user / user

[ Sample application file: ]
/sample/sample.zip

[ Running the application: ]

1. build: mvn clean install

2. run options:

2a. run embedded container / embedded db profile:

* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war,
* open http://localhost:8000/MobileSoftwareStore/

2b. run embedded container / external mysql db profile:

* MYSQL: CREATE DATABASE software_store (user: root, empty password)
* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war --spring.profiles.active=prod
* open http://localhost:8000/MobileSoftwareStore/
** you may need to adjust DB configuration in 'resources/application-prod.properties'

2c. run external container / external mysql db profile with JNDI support:
* add argument into VM options of the container: -Dspring.profiles.active=prod-jndi
* deploy to the container and run the container,
** you may need to adjust JNDI configuration in 'META-INF/context.xml'

*** Note: this application can be also deployed to external container
with embedded db main profile or external jdbc mysql profile ('prod')
(you just need to use the run arguments specified above before deploying to a container)
