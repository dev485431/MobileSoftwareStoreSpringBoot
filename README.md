<h1>Mobile Software Store - Spring Boot</h1>

This application is a software shop with mobile application files available for download.

The point of this application was to use as little configuration as possible and let Spring Boot take care of all things related to configuration.

<h2>Features for user</h2>

1. Browsing categories
2. Program files download
3. Program rating /jQuery/

<h2>Features for admin</h2>

1. Adding and uploading new programs

<h2>Other features</h2>
1. Uploading files to FTP
2. Pre and post upload file validation

<h2>Technology stack</h2>

- Java 8
- Spring Boot 1.4
- Spring Security 4.x
- Apache Commons IO
- Apache Commons Net /FTP file uploads/
- Tomcat
- Hibernate 5
- MySql
- FlywayDb
- H2 Database
- Google Gson


<h2>Deployment details</h2>

<h3>Login information:</h3>

administrator: admin / admin
developer: dev / dev
user: user / user

<h3>Sample application file:</h3>
/sample/sample.zip

<h3>Running the application:</h3>

1. <b>build:</b> mvn clean install

2. <b>run options:</b>

2a. <b>run embedded container / embedded db profile:</b>

* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war,
* open http://localhost:8000/MobileSoftwareStore/

2b. <b>run embedded container / external mysql db profile:</b>

* MYSQL: CREATE DATABASE software_store (user: root, empty password)
* java -jar MobileSoftwareStoreSpringBoot-1.0-SNAPSHOT.war --spring.profiles.active=prod
* open http://localhost:8000/MobileSoftwareStore/
** you may need to adjust DB configuration in 'resources/application-prod.properties'

2c. <b>run external container / external mysql db profile with JNDI support:</b>
* add argument into VM options of the container: -Dspring.profiles.active=prod-jndi
* deploy to the container and run the container,
** you may need to adjust JNDI configuration in 'META-INF/context.xml'

*** <b>Note:</b> this application can be also deployed to external container
with embedded db main profile or external jdbc mysql profile ('prod')
(you just need to use the run arguments specified above before deploying to a container)
