# Getting started

This chapter shows the various ways to run and deploy the application.
After the application is running (locally), you can access it under the following URLs:

- Backend: [http://localhost:8080](http://localhost:8080)
- Frontend: [http://localhost:3000](http://localhost:3000)
- The REST-API: [http://localhost:8080/api](http://localhost:8080/api)

## Start application locally for development

### Backend

The application uses Kotlin and Maven with Java Version 17.

Start application by using Maven:

```bash
mvn clean install

# using in-memory database
mvn spring-boot:run -Dspring-boot.run.profiles=test

# using locally deployed postgres as database
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Frontend

Start the frontend with yarn:

```bash
yarn --cwd ui install
yarn --cwd ui dev
```

### Database

We use PostgreSQL as our database.
To get started run the database using Docker

```bash
docker-compose up -d postgresql
```

Be aware that you need to delete the database or drop the tables and let it be regenerated on startup by Flyway in case there were changes.
If you are changing the structure of the database make sure to update or create a migration. You don't need
to do that manually. In `application-local.properties` you can uncomment the lines regarding `javax.persistence.schema-generation`.
This will create a file with the required SQL statements on startup to create the database.

## Docker

### Use pre-built docker images

The easiest way to run the application is to use the pre-built docker-images from the GitHub registry.
To start the application locally, use following docker-compose file:

```yaml
version: '3.9'
services:

  backend:
    image: ghcr.io/bfhmea4/habits-backend:latest
    container_name: habits_backend
    ports:
      - "8080:8080"
    networks:
      - net
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/habits
      SPRING_DATASOURCE_USERNAME: habits
      SPRING_DATASOURCE_PASSWORD: 'CHANGEME'
      SPRING_FLYWAY_URL: jdbc:postgresql://postgres:5432/habits
      SPRING_FLYWAY_USER: habits
      SPRING_FLYWAY_PASSWORD: 'CHANGEME'
      ALLOWED_ORIGINS: http://127.0.0.1:3000

  frontend:
    image: ghcr.io/bfhmea4/habits-frontend:latest
    container_name: habits_frontend
    ports:
      - "3000:3000"
    networks:
      - net
    environment:
      ENV_API_URL: 'http://127.0.0.1:8080'

  postgresql:
    image: postgres:13-alpine
    container_name: postgres
    networks:
      - net
    ports:
      - '5432:5432'
    environment:
      POSTGRES_DB: 'habits'
      POSTGRES_USER: 'habits'
      POSTGRES_PASSWORD: 'CHANGEME'
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data: {}

networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habits
```

Create and start container:

```bash
docker-compose up -d
```

### Build docker container manually

You can build the docker container images manually with the included Dockerfiles under `./build/package/habits` and `./ui`.
This step can be done automatically through a docker-compose file (see next chapter).

```bash
## build backend manually
docker build -f ./build/package/habits/Dockerfile . -t habits-backend:local

## build frontend manually
docker build -f ./ui/Dockerfile . -t habits-frontend:local
```

### Build docker container using docker-compose

Use following docker-compose file to build the docker images from the local source code and start the images after:

```yaml
version: '3.9'
services:

  backend:
    build:
      context: ./
      dockerfile: ./build/package/habits/Dockerfile
    container_name: habits_backend
    ports:
      - "8080:8080"
    networks:
      - net
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/habits
      SPRING_DATASOURCE_USERNAME: habits
      SPRING_DATASOURCE_PASSWORD: 'CHANGEME'
      SPRING_FLYWAY_URL: jdbc:postgresql://postgres:5432/habits
      SPRING_FLYWAY_USER: habits
      SPRING_FLYWAY_PASSWORD: 'CHANGEME'
      ALLOWED_ORIGINS: http://127.0.0.1:3000

  frontend:
    build: ./ui/
    container_name: habits_frontend
    ports:
      - "3000:3000"
    networks:
      - net
    environment:
      ENV_API_URL: 'http://127.0.0.1:8080'

  postgresql:
    image: postgres:13-alpine
    container_name: postgres
    networks:
      - net
    ports:
      - '5432:5432'
    environment:
      POSTGRES_DB: 'habits'
      POSTGRES_USER: 'habits'
      POSTGRES_PASSWORD: 'CHANGEME'
    volumes:
      - db-data:/var/lib/postgresql/data

volumes:
  db-data: {}

networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habits
```

Build and start container (this takes some time):

```bash
docker-compose up -d
```

### Kubernetes

You can deploy Habits in your Kubernetes cluster, but you have to set all the env variables.

#### Environment Variables

You need to set the following environment:

**Backend**

| ENV                           | Description                                                     |
|-------------------------------|-----------------------------------------------------------------|
| `SPRING_DATASOURCE_URL`       | The JDBC-String of the database                                 |
| `SPRING_DATASOURCE_USERNAME`  | The database user                                               |
| `SPRING_DATASOURCE_PASSWORD`  | Password of the database user                                   |
| `SPRING_FLYWAY_URL`           | The JDBC-String of the database                                 |
| `SPRING_FLYWAY_USER`          | The flyway database user                                        |
| `SPRING_FLYWAY_PASSWORD`      | Password of the flyway database user                            |
| `ALLOWED_ORIGINS`             | The allowed origins for CORS, e.g. `https://template.habits.io` |

**UI**

| ENV                | Description                                                                                                           |
|--------------------|-----------------------------------------------------------------------------------------------------------------------|
| `ENV_API_URL`      | The URL of the API, e.g. `https://template.habits.io` (without trailing slash, must be accessible from the webclient) |


## Postman

Have a look at `src/main/resources/postman` for a Postman collection to test the API.
Import it and run the login request. It will fetch a token and automatically set it for all other requests.
Also make sure to import the environments to be able to run the requests against local or production.
