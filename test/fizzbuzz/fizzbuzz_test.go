package test

import (
	"testing"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz/server"
	"github.com/pocketbase/pocketbase/tests"
)

func TestFizzBuzz(t *testing.T) {
	tests := []struct {
		description string
		i           int
		want        string
	}{
		{description: "get String of number if number doesn't match /3 or /5", i: 1, want: "1"},
		{description: "get buzz if number matches /5", i: 5, want: "buzz"},
		{description: "get fizz if number matches /3", i: 6, want: "fizz"},
		{description: "get fizzbuzz if number matches /3 and /5", i: 15, want: "fizzbuzz"},
	}
	for _, test := range tests {
		t.Run(test.description, func(t *testing.T) {
			got := fizzbuzz.Calculate(test.i)
			if got != test.want {
				t.Errorf("got %q, want %q", got, test.want)
			}
		})
	}
}

func TestHttpController(t *testing.T) {
	// setup the test app instance
	setupTestApp := func() (*tests.TestApp, error) {
		app, err := tests.NewTestApp()
		if err != nil {
			return nil, err
		}

		server.BindAppHooks(app)

		return app, nil
	}

	scenarios := []tests.ApiScenario{
		// First test case
		{
			Name:            "get HTTP status 200, and body returns the number 1 with parameter 1",
			Url:             "/api/1",
			ExpectedStatus:  200,
			ExpectedContent: []string{"1"},
			TestAppFactory:  setupTestApp,
		},
		// Second test case
		{
			Name:            "get HTTP status 404, when route does not exists",
			Url:             "/non-route/not-found",
			ExpectedStatus:  404,
			ExpectedContent: []string{"\"data\":{}"},
			TestAppFactory:  setupTestApp,
		},
		// Third test case
		{
			Name:            "get HTTP status 400, when param is not a number",
			Url:             "/api/not-a-number",
			ExpectedStatus:  400,
			ExpectedContent: []string{"Invalid number"},
			TestAppFactory:  setupTestApp,
		},
	}

	// Run the test cases
	for _, scenario := range scenarios {
		scenario.Test(t)
	}

}
