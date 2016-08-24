[ Users: ]

administrator: admin / admin
developer: dev / dev
user: user / user

[ Sample application file: ]
/sample/sample.zip

[ Running the application: ]

1. build: mvn clean install

2a. run embedded container / embedded db profile:

* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war,
* open http://localhost:8000/

2b. run embedded container / external mysql db profile:

* MYSQL: CREATE DATABASE software_store (user: root, empty password)
* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war --spring.profiles.active=prod
* open http://localhost:8000/

2c. run external container / external mysql db profile with JNDI support:
* deploy to external container
* add argument into VM options: -Dspring.profiles.active=prod-jndi
