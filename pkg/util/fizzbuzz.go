package util

import (
	"fmt"
	"log"
)

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
	log.Println("Fizzbuzz - Input:", i, "Output:", res)
	return res
}
