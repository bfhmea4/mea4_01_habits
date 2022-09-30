package main

import (
	"os"
	"strconv"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/logger"
)

func main() {
	app := fiber.New(fiber.Config{})

	app.Use(cors.New(cors.Config{
		AllowMethods:     "GET,POST,PUT,DELETE,OPTIONS",
		AllowCredentials: true,
		AllowOrigins:     "*",
	}))

	app.Use(logger.New(logger.Config{
		TimeFormat: "2006/01/02 - 15:04:05",
		TimeZone:   "Europe/Zurich",
	}))

	app.Get("/:number", func(c *fiber.Ctx) error {
		number, err := strconv.Atoi(c.Params("number"))

		if err != nil {
			return c.Status(400).SendString("Invalid number")
		}

		return c.SendString(fizzbuzz.Calculate(number))
	})

	if err := app.Listen(":8000"); err != nil {
		os.Exit(1)
	}
}
