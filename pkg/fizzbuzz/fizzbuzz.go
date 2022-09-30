package fizzbuzz

import "fmt"

func Calculate(i int) string {
	fizz := "fizz"
	buzz := "buzz"

	if i%3 == 0 && i%5 == 0 {
		return fizz + buzz
	}
	if i%3 == 0 {
		return fizz
	}
	if i%5 == 0 {
		return buzz
	}
	return fmt.Sprintf("%d", i)
}
