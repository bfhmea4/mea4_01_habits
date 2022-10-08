# Pocketbase backend build
FROM golang:1.19 as build
WORKDIR /go/src/github.com/bfhmea4/mea4_01_habits
# RUN go get -d -v golang.org/x/net/html
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o habitsus ./cmd/habitsus

# Production build
FROM alpine:latest

WORKDIR /app

RUN addgroup -g 1001 -S habitsus
RUN adduser -S habitsus -u 1001

RUN apk --no-cache add ca-certificates

COPY --from=build --chown=habitsus:habitsus /go/src/github.com/bfhmea4/mea4_01_habits/habitsus ./

EXPOSE 8090

CMD ["./habitsus", "serve", "--http=0.0.0.0:8090"]
