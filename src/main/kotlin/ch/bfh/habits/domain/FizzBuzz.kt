package ch.bfh.habits.domain

fun fizzbuzz(n: Int): String {
    var result = ""
    if (n % 3 == 0) result = "Fizz"
    if (n % 5 == 0) result += "Buzz"
    return result.ifEmpty { n.toString() }
}
