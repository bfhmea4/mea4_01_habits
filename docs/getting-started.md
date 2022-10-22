# Getting started

This chapter shows the various ways to run/ deploy the application.
After the application is running (locally), you can access it under the following URLs:

- Backend: [http://localhost:8080](http://localhost:8080)
- Frontend: [http://localhost:3000](http://localhost:3000)
- The REST-API: [http://localhost:8080/api](http://localhost:8080/api)

## Test application locally

### Build Local Image

```
mvn spring-boot:build-image -DskipTests=True -Dspring-boot.build-image.imageName=bfh/habits
```

### Run Local Image

```
docker run -p 8080:8080 -t bfh/habits
```

Run test suite against localhost

```
mvn test -Dhabits.test.localhost=true
```

### Backend

The application uses kotlin, maven and java version 17.

Start application by using maven:
```
mvn spring-boot:run
```

### Frontend

Start the frontend directly:

```bash
yarn --cwd ui/app dev
```

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
      - "8090:8090"
    volumes:
      - pb_data:/pb_data
    networks:
      - net

  ui:
    image: ghcr.io/bfhmea4/habits-ui:latest
    container_name: habits_frontend
    ports:
      - "3000:3000"
    environment:
      - ENV_API_URL=http://127.0.0.1:8090

volumes:
  pb_data: {}

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
docker build -f ./ui/app/Dockerfile . -t habits-ui:local
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
      - "8090:8090"
    volumes:
      - pb_data:/pb_data
    networks:
      - net

  ui:
    build: ./ui/app/
    container_name: habits_frontend
    ports:
      - "3000:3000"
    environment:
      - ENV_API_URL=http://127.0.0.1:8090

volumes:
  pb_data: {}

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
