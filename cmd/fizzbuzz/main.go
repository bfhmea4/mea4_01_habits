package main

import (
	"log"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz/env"
	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz/server"
)

func init() {
	err := env.Init()
	if err != nil {
		log.Fatal(err)
	}
}

func main() {
	app := server.Setup()

	server.BindAppHooks(app)

	// start the pocketbase server
	if err := app.Start(); err != nil {
		log.Fatal(err)
	}
}
