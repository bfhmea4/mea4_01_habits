# Getting started

This chapter shows the various ways to run/ deploy the application.
After the application is running (locally), you can access it under the following URLs:

- Backend: [http://localhost:8080](http://localhost:8080)
- Frontend: [http://localhost:3000](http://localhost:3000)
- The REST-API: [http://localhost:8080/api](http://localhost:8080/api)

## Test application locally

### Build Local Image

```bash
docker build -f ./build/package/habits/Dockerfile . -t habits-backend:local
```

### Run Local Image

Start docker image of backend:

```bash
docker run -p 8080:8080 -t habits-backend:local
```

Run test suite against localhost:

```bash
mvn test -Dhabits.test.localhost=true
```

### Backend

The application uses kotlin, maven and java version 17.

Start application by using maven:

```bash
mvn spring-boot:run
```

### Frontend

Start the frontend directly:

```bash
yarn --cwd ui/app dev
```

### Database

By default, we use PostgreSQL as our database.
You have to configure the database connection settings in the following file: `src/main/kotlin/resources/application.properties`.
The default connection string is `jdbc:postgresql://postgres:5432/habits`, which works with the local docker-compose file (this includes the postgres deployment).


## Docker

### Use pre-builded docker images

The easiest way to run the application is to use the pre-builed docker-images from the Github registry.
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

  frontend:
    image: ghcr.io/bfhmea4/habits-frontend:latest
    container_name: habits_frontend
    ports:
      - "3000:3000"
    networks:
    - net
    environment:
      - ENV_API_URL=http://127.0.0.1:8080

  postgresql:
    image: postgres:13-alpine
    container_name: postgres
    networks:
    - net
    ports:
    - '5432:5432'
    environment:
      POSTGRES_DB: 'habits'
      POSTGRES_PASSWORD: 'habits'
      POSTGRES_USER: 'habits'
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

You can build the docker container images manually with the included Dockerfiles under `./build/package/habits` and `./ui/app`.
This step can be done automatically through a docker-compose file (see next chaptre).

```bash
## build backend manually
docker build -f ./build/package/habits/Dockerfile . -t habits-backend:local

## build frontend manually
docker build -f ./ui/app/Dockerfile . -t habits-frontend:local
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

  frontend:
    build: ./ui/app/
    container_name: habits_frontend
    ports:
      - "3000:3000"
    networks:
    - net
    environment:
      - ENV_API_URL=http://127.0.0.1:8080

  postgresql:
    image: postgres:13-alpine
    container_name: postgres
    networks:
    - net
    ports:
    - '5432:5432'
    environment:
      POSTGRES_DB: 'habits'
      POSTGRES_PASSWORD: 'habits'
      POSTGRES_USER: 'habits'
    volumes:
    - db-data:/var/lib/postgresql/data

volumes:
  db-data: {}

networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habits
```

Build and start container (this take some time):

```bash
docker-compose up -d
```

### Kubernetes

You can deploy Habits in your Kubernetes cluster, but you have to set all the env variables.

#### Environment Variables

You need to set the following environment:

**UI**

- `ENV_API_URL` - The URL of the API, e.g. `https://template.habits.io` (without trailing slash, but /api at the end, must be accessible from the webclient)
