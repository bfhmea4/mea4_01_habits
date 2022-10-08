package main

import (
	"log"

	"github.com/bfhmea4/mea4_01_habits/pkg/env"
	"github.com/bfhmea4/mea4_01_habits/pkg/server"
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
