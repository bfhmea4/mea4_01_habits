# Getting started

This chapter shows the various ways to run/ deploy the application.
After the application is running (locally), you can access it under the following URLs:
- Backend Admin-Interface (PocketBase): [http://localhost:8090/_/](http://localhost:8090/_/)
- Frontend: [http://localhost:3000](http://localhost:3000)
- The REST-API: [http://localhost:8090/api](http://localhost:8090/api)

**Next steps**

After the application is started, you have to create some users to test the application. See the following [guide](https://bfhmea4.github.io/mea4_01_habits/create-users/).

## Test application locally

If you want to test some new code without building docker images, you can run the backend and frontend directly with `go` and `yarn`.
This is only recommended for testing and development purposes as to the performance isn't as good as built binaries.

### Backend

Start the backend directly (remember to set the `POCKETBASE_DATA_DIR` env variable):

```bash
export POCKETBASE_DATA_DIR="./pb-data"
go run cmd/habitsus/main.go serve
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
    image: ghcr.io/bfhmea4/habitsus-backend:latest
    container_name: habitsus_backend
    ports:
      - "8090:8090"
    volumes:
      - pb_data:/pb_data
    networks:
      - net

  ui:
    image: ghcr.io/bfhmea4/habitsus-ui:latest
    container_name: habitsus_frontend
    ports:
      - "3000:3000"
    environment:
      - ENV_API_URL=http://127.0.0.1:8090

volumes:
  pb_data: {}

networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habitsus
```

Create and start container:

```bash
docker-compose up -d
```

### Build docker container manually

You can build the docker container images manually with the included Dockerfiles under `./build/package/habitsus` and `./ui/app`.
This step can be done automatically through a docker-compose file (see next chaptre).

```bash
## build backend manually
docker build -f ./build/package/habitsus/Dockerfile . -t habitsus-backend:local

## build frontend manually
docker build -f ./ui/app/Dockerfile . -t habitsus-ui:local
```

### Build docker container using docker-compose

Use following docker-compose file to build the docker images from the local source code and start the images after:

```yaml
version: '3.9'
services:

  backend:
    build:
      context: ./
      dockerfile: ./build/package/habitsus/Dockerfile
    container_name: habitsus_backend
    ports:
      - "8090:8090"
    volumes:
      - pb_data:/pb_data
    networks:
      - net

  ui:
    build: ./ui/app/
    container_name: habitsus_frontend
    ports:
      - "3000:3000"
    environment:
      - ENV_API_URL=http://127.0.0.1:8090

volumes:
  pb_data: {}

networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habitsus
```

Build and start container (this take some time):

```bash
docker-compose up -d
```

### Kubernetes

You can deploy Habitsus in your Kubernetes cluster, but you have to set all the env variables.

#### Environment Variables

You need to set the following environment:

**Backend**

- `POCKETBASE_DATA_DIR` - The directory where the PocketBase data is stored. Default: `/pb_data`
- `POCKETBASE_ENCRYPTION_KEY` - The encryption key for the PocketBase database. Must be 32 characters long.

**UI**

- `ENV_API_URL` - The URL of the API, e.g. `https://template.habitsus.io` (without trailing slash, but /api at the end, must be accessible from the webclient)
