package fizzbuzz

import (
	"github.com/gofiber/fiber/v2"
)

func ServeHTTP() fiber.App {
	app := fiber.New()
	app.Get("/", func(c *fiber.Ctx) error {
		// return status 200 with "not implemented" as body
		return c.SendStatus(fiber.StatusNotImplemented)
	})
	return *app
}

func Calculate(i int) string {
	return "not implemented"
}
