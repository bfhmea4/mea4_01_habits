package util

import "fmt"

func CalculateFizzbuzz(i int) string {
	res := ""

	if i%3 == 0 {
		res += "fizz"
	}
	if i%5 == 0 {
		res += "buzz"
	}
	if res == "" {
		res += fmt.Sprintf("%d", i)
	}
	return res
}
