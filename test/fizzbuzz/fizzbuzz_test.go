package test

import (
	"testing"

	"github.com/bfhmea4/mea4_01_habits/pkg/fizzbuzz"
)

func TestFizzBuzz(t *testing.T) {
	tests := []struct {
		name string
		i    int
		want string
	}{
		{name: "Test1", i: 1, want: "1"},
		{name: "Test2", i: 2, want: "2"},
		{name: "Test3", i: 3, want: "fizz"},
		{name: "Test4", i: 4, want: "4"},
		{name: "Test5", i: 5, want: "buzz"},
		{name: "Test6", i: 6, want: "fizz"},
		{name: "Test7", i: 7, want: "7"},
		{name: "Test8", i: 8, want: "8"},
		{name: "Test9", i: 9, want: "fizz"},
		{name: "Test10", i: 10, want: "buzz"},
		{name: "Test11", i: 11, want: "11"},
		{name: "Test12", i: 12, want: "fizz"},
		{name: "Test13", i: 13, want: "13"},
		{name: "Test14", i: 14, want: "14"},
		{name: "Test15", i: 15, want: "fizzbuzz"},
		{name: "Test16", i: 16, want: "16"},
		{name: "Test17", i: 17, want: "17"},
		{name: "Test18", i: 18, want: "fizz"},
		{name: "Test19", i: 19, want: "19"},
		{name: "Test20", i: 20, want: "buzz"},
		{name: "Test21", i: 21, want: "fizz"},
		{name: "Test22", i: 22, want: "22"},
		{name: "Test23", i: 23, want: "23"},
		{name: "Test24", i: 24, want: "fizz"},
		{name: "Test25", i: 25, want: "buzz"},
		{name: "Test26", i: 26, want: "26"},
		{name: "Test27", i: 27, want: "fizz"},
		{name: "Test28", i: 28, want: "28"},
		{name: "Test29", i: 29, want: "29"},
		{name: "Test30", i: 30, want: "fizzbuzz"},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := fizzbuzz.Calculate(tt.i); got != tt.want {
				t.Errorf("FizzBuzz() = %v, want %v", got, tt.want)
			}
		})
	}
}
