package server

import (
	"net/http"
	"strconv"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz/env"
	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz/migrations"
	"github.com/labstack/echo/v5"
	"github.com/pocketbase/pocketbase"
	"github.com/pocketbase/pocketbase/core"
)

func BindAppHooks(app core.App) {
	app.OnBeforeServe().Add(func(e *core.ServeEvent) error {
		e.Router.AddRoute(echo.Route{
			Method: http.MethodGet,
			Path:   "/api/:number",
			Handler: func(c echo.Context) error {
				number, err := strconv.Atoi(c.PathParam("number"))
				if err != nil {
					return c.String(400, "Invalid number")
				}
				return c.String(200, fizzbuzz.Calculate(number))
			},
		})

		return nil
	})
}

// Setup initializes the pocketbase server
func Setup() *pocketbase.PocketBase {

	// initialize pocketbase collections
	migrations.InitCollections()

	// initialize pocketbase server
	app := pocketbase.NewWithConfig(pocketbase.Config{
		DefaultDataDir:       env.POCKETBASE_DATA_DIR,
		DefaultEncryptionEnv: env.POCKETBASE_ENCRYPTION_KEY,
	})

	return app
}
