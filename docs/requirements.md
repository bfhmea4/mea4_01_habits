# Requirements
To find your way around the environment, you need certain know-how but also requirements for the client device so that you can connect at all.

## :books: Know-How
| topic | requirement |
| -- | -- |
| **Git** | Knows the most important Git commands |
| | GPG `signed commits` are **enforced** |
| **GitHub** | Knows the most important GitHub functions: |
| | - `GitHub Issues` |
| | - `GitHub Pull Requests` |
| | - `GitHub Workflows` |
| | - `GitHub Releases` |
| | - `GitHub Repositories` |
| **Docker** | The application is dockerized and stateless |
| | Knows the most important `docker` commands |
| **Kubernetes** | Knows the concept of Kubernetes |
| | Knows the most important `kubectl` commands |

## Setup pre-commit
We use pre-commit to make several checks to identifying some issues on every commit.

1. [install the pre-commit binary](https://pre-commit.com/#install)

2. Setup pre-commit for this repository:

```bash
// go to the root of the cloned git repository
$ cd mea4_01_habits
$ pre-commit install
pre-commit installed at .git/hooks/pre-commit
```

## Test backend locally
Start the backend directly (remember to set the POCKETBASE_DATA_DIR env variable):

```bash
go run cmd/habitsus/main.go serve
```

### Docker
Build docker container of the backend:

```bash
docker build -f ./build/package/habitsus/Dockerfile . -t habitsus:local
```

Use following docker-compose file:

```yaml
version: '3.8'
services:
  backend:
    image: habitsus:local
    restart: unless-stopped
    container_name: backend
    networks:
    - net
    ports:
    - '8000:8000'
networks:
  net:
    driver_opts:
      com.docker.network.bridge.name: habitsus
```

Create and start container:

```bash
docker-compose up -d
```
