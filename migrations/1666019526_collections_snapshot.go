package migrations

import (
	"encoding/json"

	"github.com/pocketbase/dbx"
	"github.com/pocketbase/pocketbase/daos"
	m "github.com/pocketbase/pocketbase/migrations"
	"github.com/pocketbase/pocketbase/models"
)

// Auto generated migration with the most recent collections configuration.
func init() {
	m.Register(func(db dbx.Builder) error {
		jsonData := `[
			{
				"id": "7ge5qq2bzel81as",
				"created": "2022-10-08 18:33:53.902",
				"updated": "2022-10-17 15:11:08.428",
				"name": "fizzbuzz",
				"system": false,
				"schema": [
					{
						"system": false,
						"id": "k5hbhmyh",
						"name": "input",
						"type": "number",
						"required": true,
						"unique": false,
						"options": {
							"min": null,
							"max": null
						}
					}
				],
				"listRule": "",
				"viewRule": "",
				"createRule": "",
				"updateRule": "",
				"deleteRule": ""
			},
			{
				"id": "systemprofiles0",
				"created": "2022-10-08 18:51:37.637",
				"updated": "2022-10-17 15:11:08.428",
				"name": "profiles",
				"system": true,
				"schema": [
					{
						"system": true,
						"id": "pbfielduser",
						"name": "userId",
						"type": "user",
						"required": true,
						"unique": true,
						"options": {
							"maxSelect": 1,
							"cascadeDelete": true
						}
					},
					{
						"system": false,
						"id": "pbfieldname",
						"name": "name",
						"type": "text",
						"required": false,
						"unique": false,
						"options": {
							"min": null,
							"max": null,
							"pattern": ""
						}
					},
					{
						"system": false,
						"id": "pbfieldavatar",
						"name": "avatar",
						"type": "file",
						"required": false,
						"unique": false,
						"options": {
							"maxSelect": 1,
							"maxSize": 5242880,
							"mimeTypes": [
								"image/jpg",
								"image/jpeg",
								"image/png",
								"image/svg+xml",
								"image/gif"
							],
							"thumbs": null
						}
					}
				],
				"listRule": "userId = @request.user.id",
				"viewRule": "userId = @request.user.id",
				"createRule": "userId = @request.user.id",
				"updateRule": "userId = @request.user.id",
				"deleteRule": null
			},
			{
				"id": "j8xg36hwgdk3wuc",
				"created": "2022-10-14 11:52:50.319",
				"updated": "2022-10-17 15:11:08.428",
				"name": "habits",
				"system": false,
				"schema": [
					{
						"system": false,
						"id": "hwl1vovf",
						"name": "author",
						"type": "user",
						"required": false,
						"unique": false,
						"options": {
							"maxSelect": 1,
							"cascadeDelete": false
						}
					},
					{
						"system": false,
						"id": "qbtdgakx",
						"name": "title",
						"type": "text",
						"required": true,
						"unique": false,
						"options": {
							"min": null,
							"max": null,
							"pattern": ""
						}
					},
					{
						"system": false,
						"id": "osabrxwd",
						"name": "description",
						"type": "text",
						"required": false,
						"unique": false,
						"options": {
							"min": null,
							"max": null,
							"pattern": ""
						}
					},
					{
						"system": false,
						"id": "lurulnbq",
						"name": "amount_weekly",
						"type": "number",
						"required": false,
						"unique": false,
						"options": {
							"min": null,
							"max": null
						}
					},
					{
						"system": false,
						"id": "vu7dfwb2",
						"name": "compleated",
						"type": "bool",
						"required": false,
						"unique": false,
						"options": {}
					},
					{
						"system": false,
						"id": "1bouvvbh",
						"name": "image",
						"type": "file",
						"required": false,
						"unique": false,
						"options": {
							"maxSelect": 1,
							"maxSize": 5242880,
							"mimeTypes": [
								"image/jpg",
								"image/jpeg",
								"image/png",
								"image/svg+xml",
								"image/gif"
							],
							"thumbs": []
						}
					}
				],
				"listRule": "",
				"viewRule": "",
				"createRule": "",
				"updateRule": "",
				"deleteRule": ""
			},
			{
				"id": "immgp856pwstevf",
				"created": "2022-10-14 12:02:58.672",
				"updated": "2022-10-17 15:11:17.548",
				"name": "journal_entries",
				"system": false,
				"schema": [
					{
						"system": false,
						"id": "yxjkdyly",
						"name": "habit_id",
						"type": "relation",
						"required": false,
						"unique": false,
						"options": {
							"maxSelect": 1,
							"collectionId": "j8xg36hwgdk3wuc",
							"cascadeDelete": false
						}
					},
					{
						"system": false,
						"id": "8bawjveu",
						"name": "description",
						"type": "text",
						"required": false,
						"unique": false,
						"options": {
							"min": null,
							"max": null,
							"pattern": ""
						}
					},
					{
						"system": false,
						"id": "1flmcbgs",
						"name": "author",
						"type": "user",
						"required": false,
						"unique": false,
						"options": {
							"maxSelect": 1,
							"cascadeDelete": false
						}
					}
				],
				"listRule": "",
				"viewRule": "",
				"createRule": "",
				"updateRule": "",
				"deleteRule": ""
			}
		]`

		collections := []*models.Collection{}
		if err := json.Unmarshal([]byte(jsonData), &collections); err != nil {
			return err
		}

		return daos.New(db).ImportCollections(collections, true, nil)
	}, func(db dbx.Builder) error {
		// no revert since the configuration on the environment, on which
		// the migration was executed, could have changed via the UI/API
		return nil
	})
}
