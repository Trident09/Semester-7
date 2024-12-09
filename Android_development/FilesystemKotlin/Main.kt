import java.io.File

fun main() {
    val fileName = "example.txt"
    val file = File(fileName)

    // Step 1: Create a file
    try {
        if (file.createNewFile()) {
            println("File created successfully: ${file.name}")
        } else {
            println("File already exists: ${file.name}")
        }
    } catch (e: Exception) {
        println("An error occurred while creating the file: ${e.message}")
    }

    // Pause for 2 seconds
    Thread.sleep(2000)

    // Step 2: Write data to the file
    try {
        file.writeText("Hello, this is a sample file created in Kotlin.\n")
        file.appendText("This is the second line of the file.")
        println("Data written to file successfully.")
    } catch (e: Exception) {
        println("An error occurred while writing to the file: ${e.message}")
    }

    // Pause for 2 seconds
    Thread.sleep(2000)

    // Step 3: Read data from the file
    try {
        val fileContent = file.readText()
        println("File Contents:\n$fileContent")
    } catch (e: Exception) {
        println("An error occurred while reading the file: ${e.message}")
    }

    // Pause for 2 seconds
    Thread.sleep(2000)

    // Step 4: Delete the file
    try {
        if (file.delete()) {
            println("File deleted successfully.")
        } else {
            println("File deletion failed.")
        }
    } catch (e: Exception) {
        println("An error occurred while deleting the file: ${e.message}")
    }
}
