package test

import (
	"bytes"
	"io"
	"net/http"
	"strings"
	"testing"
)

func TestHabitsListEmpty(t *testing.T) {
	response, err := http.Get("http://127.0.0.1:8090/api/collections/habits/records")

	if err != nil {
		t.Log("Check that the application is running")
		t.FailNow()
	}

	defer response.Body.Close()
	bodyBytes, _ := io.ReadAll(response.Body)
	got := strings.TrimRight(string(bodyBytes), "\n")

	want := `{"page":1,"perPage":30,"totalItems":0,"totalPages":0,"items":[]}`

	if got != want {
		t.Errorf("got %q, want %q", got, want)
	}
}

func TestHabitsCreate(t *testing.T) {
	var jsonData = []byte(`{
		"id": "y9s8nj77ubux0tj",
		"title": "Test",
		"description": "test"
	}`)
	request, err := http.NewRequest("POST", "http://127.0.0.1:8090/api/collections/habits/records", bytes.NewBuffer(jsonData))

	if err != nil {
		t.Log("Check that the application is running")
		t.FailNow()
	}

	request.Header.Set("Content-Type", "application/json; charset=UTF-8")

	client := &http.Client{}
	response, err := client.Do(request)

	if err != nil {
		t.Log("Post request failed")
		t.FailNow()
	}

	defer response.Body.Close()
	bodyBytes, _ := io.ReadAll(response.Body)
	got := strings.TrimRight(string(bodyBytes), "\n")

	shouldContain1 := `"title":"Test"`
	shouldContain2 := `"description":"test"`

	if !strings.Contains(got, shouldContain1) || !strings.Contains(got, shouldContain2) {
		t.Errorf("got %q, should contain %q and %q", got, shouldContain1, shouldContain2)
	}
}

func TestHabitsList(t *testing.T) {
	response, err := http.Get("http://127.0.0.1:8090/api/collections/habits/records")

	if err != nil {
		t.Log("Check that the application is running")
		t.FailNow()
	}

	defer response.Body.Close()
	bodyBytes, _ := io.ReadAll(response.Body)
	got := strings.TrimRight(string(bodyBytes), "\n")

	shouldContain := `"totalItems":1`

	if !strings.Contains(got, shouldContain) {
		t.Errorf("got %q, should contain %q", got, shouldContain)
	}
}

func TestHabitsDelete(t *testing.T) {
	request, err := http.NewRequest("DELETE", "http://127.0.0.1:8090/api/collections/habits/records/y9s8nj77ubux0tj", nil)

	if err != nil {
		t.Log("Check that the application is running")
		t.FailNow()
	}

	client := &http.Client{}
	response, err := client.Do(request)

	if err != nil {
		t.Log("Delete request failed")
		t.FailNow()
	}

	defer response.Body.Close()

	got := response.StatusCode
	want := 204

	if got != want {
		t.Errorf("Status code: got %q, want %q", got, want)
	}
}
