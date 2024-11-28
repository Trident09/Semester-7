fun main() {
    print("Enter a number: ")
    val number = readLine()?.toIntOrNull() ?: 0

    // Using if-else to check if the number is positive, negative, or zero
    if (number > 0) {
        println("The number is positive.")
    } else if (number < 0) {
        println("The number is negative.")
    } else {
        println("The number is zero.")
    }

    // Using when expression to determine the range of the number
    when (number) {
        in Int.MIN_VALUE..-1 -> println("The number is in the negative range.")
        0 -> println("The number is exactly zero.")
        in 1..10 -> println("The number is between 1 and 10.")
        in 11..100 -> println("The number is between 11 and 100.")
        else -> println("The number is greater than 100.")
    }
}