package test

import (
	"net/http/httptest"
	"testing"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
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
	tests := []struct {
		description  string // description of the test case
		route        string // route path to test
		param        string // route parameter to test
		expectedCode int    // expected HTTP status code
		expectedBody string // expected HTTP response body
	}{
		// First test case
		{
			description:  "get HTTP status 200, and body returns the number 1 with parameter 1",
			route:        "/",
			param:        "1",
			expectedCode: 200,
			expectedBody: "1",
		},
		// Second test case
		{
			description:  "get HTTP status 404, when route is not exists",
			route:        "/non-route/not-found",
			expectedCode: 404,
		},
		// Third test case
		{
			description:  "get HTTP status 400, when param is not a number",
			route:        "/",
			param:        "not-a-number",
			expectedCode: 400,
		},
	}

	// Register the http server from main function
	app := fizzbuzz.ServeHTTP()

	// Run the test cases
	for _, test := range tests {
		req := httptest.NewRequest("GET", test.route, nil)
		req.Header.Set("Content-Type", "application/json")
		req.Header.Set("Accept", "application/json")
		if test.param != "" {
			req = httptest.NewRequest("GET", test.route+test.param, nil)
		}
		res, err := app.Test(req)
		if err != nil {
			t.Fatal(err)
		}
		if res.StatusCode != test.expectedCode {
			t.Errorf("Test case %s failed, expected status code %d, got %d", test.description, test.expectedCode, res.StatusCode)
		}
	}

}
