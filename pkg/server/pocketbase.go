package server

import (
	"bytes"
	"encoding/json"
	"log"
	"net/http"
	"strconv"

	"github.com/bfhmea4/mea4_01_habits/pkg/env"
	"github.com/bfhmea4/mea4_01_habits/pkg/util"
	"github.com/labstack/echo/v5"
	"github.com/pocketbase/pocketbase"
	"github.com/pocketbase/pocketbase/core"
)

func BindAppHooks(app core.App) {
	app.OnBeforeServe().Add(func(e *core.ServeEvent) error {
		//nolint:errcheck
		e.Router.AddRoute(echo.Route{
			Method: http.MethodGet,
			Path:   "/api/:number",
			Handler: func(c echo.Context) error {
				log.Println("Fizzbuzz endpoint hit - Path parameter:", c.PathParam("number"))
				number, err := strconv.Atoi(c.PathParam("number"))
				if err != nil {
					return c.String(400, "Invalid number")
				}

				// ToDo remove after we start on the real thing
				values := map[string]interface{}{"input": number}
				json_data, err := json.Marshal(values)

				if err != nil {
					log.Fatal(err)
				}

				http.Post("http://127.0.0.1:8090/api/collections/fizzbuzz/records", "application/json",
					bytes.NewBuffer(json_data))

				return c.String(200, util.CalculateFizzbuzz(number))
			},
		})

		return nil
	})
}

// Setup initializes the pocketbase server
func Setup() *pocketbase.PocketBase {
	// initialize pocketbase server
	app := pocketbase.NewWithConfig(pocketbase.Config{
		DefaultDataDir:       env.POCKETBASE_DATA_DIR,
		DefaultEncryptionEnv: env.POCKETBASE_ENCRYPTION_KEY,
	})

	return app
}
