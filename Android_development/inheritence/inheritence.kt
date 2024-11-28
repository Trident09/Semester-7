open class Animal(val name: String, val age: Int) {
    // Function in base class
    open fun sound() {
        println("This is a generic animal sound.")
    }

    fun showDetails() {
        println("Animal Name: $name, Age: $age")
    }
}

// Derived class inheriting from Animal
class Dog(name: String, age: Int) : Animal(name, age) {
    // Overriding the sound method
    override fun sound() {
        println("The dog barks.")
    }

    // Additional function in derived class
    fun fetch() {
        println("$name is fetching the ball!")
    }
}

fun main() {
    // Creating an instance of the base class
    val genericAnimal = Animal("Animal", 5)
    genericAnimal.showDetails()
    genericAnimal.sound()

    println()

    // Creating an instance of the derived class
    val dog = Dog("Buddy", 3)
    dog.showDetails()
    dog.sound()
    dog.fetch()
}