package fizzbuzz

import (
	"fmt"
	"strconv"

	"github.com/gofiber/fiber/v2"
	"github.com/gofiber/fiber/v2/middleware/cors"
	"github.com/gofiber/fiber/v2/middleware/logger"
)

func ServeHTTP() *fiber.App {
	app := fiber.New()

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

		return c.SendString(Calculate(number))
	})

	return app
}

func Calculate(i int) string {
	fizz := "fizz"
	buzz := "buzz"

	if i%3 == 0 && i%5 == 0 {
		return fizz + buzz
	}
	if i%3 == 0 {
		return fizz
	}
	if i%5 == 0 {
		return buzz
	}
	return fmt.Sprintf("%d", i)
}
