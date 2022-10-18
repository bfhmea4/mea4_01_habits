#!/bin/bash

cd ./test/data
find . ! -name '.gitkeep' -type f -exec rm -f {} +

cd ../../
go run cmd/habitsus/main.go serve --dir="./test/data"
