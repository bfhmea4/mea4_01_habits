package main

import (
	"log"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
)

func main() {
	app := fizzbuzz.ServeHTTP()

	if err := app.Listen(":8000"); err != nil {
		log.Fatal(err)
	}
}
