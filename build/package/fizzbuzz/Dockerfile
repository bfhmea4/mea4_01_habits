# Pocketbase backend build
FROM golang:1.19 as build
WORKDIR /go/src/github.com/natrongmbh/mea4_01_habits
# RUN go get -d -v golang.org/x/net/html
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o fizzbuzz ./cmd/fizzbuzz

# Production build
FROM alpine:latest

WORKDIR /app

RUN addgroup -g 1001 -S fizzbuzz
RUN adduser -S fizzbuzz -u 1001

RUN apk --no-cache add ca-certificates

COPY --from=build --chown=fizzbuzz:fizzbuzz /go/src/github.com/natrongmbh/mea4_01_habits/fizzbuzz ./

USER 1001

EXPOSE 8000

CMD ["./fizzbuzz"]
