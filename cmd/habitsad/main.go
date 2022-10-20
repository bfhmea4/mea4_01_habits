package main

import (
	"log"

	_ "github.com/bfhmea4/mea4_01_habits/migrations"
	"github.com/bfhmea4/mea4_01_habits/pkg/env"
	"github.com/bfhmea4/mea4_01_habits/pkg/logger"
	"github.com/bfhmea4/mea4_01_habits/pkg/server"
)

func init() {
	err := env.Init()
	if err != nil {
		log.Fatal(err)
	}
}

func main() {
	logger.SetupLogger()

	app := server.Setup()
	server.BindAppHooks(app)

	// start the pocketbase server
	if err := app.Start(); err != nil {
		log.Fatal(err)
	}
}
