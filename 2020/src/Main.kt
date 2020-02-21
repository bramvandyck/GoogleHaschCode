import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream

data class Book(val id: Int, val score: Int) {}

data class Library(
    val id: Int,
    val books: List<Book>,
    val timeToSignUp: Int,
    val numberOfBooksEachDay: Int,
    var score: Int = 0
) {}

fun main() {
    for (ex in 'A'..'F') {
        println("${ex}: started")
        val input = File("in/${ex}.in");
        val output = File("src/${ex}.out");
        solve(reader = input.bufferedReader(), output = output);
        println("${ex}: done")
        println()
    }
}


fun solve(reader: BufferedReader, output: File) {

    // INPUT
    var (_, numberOfLibraries, numberOfDays) = reader.readLine().split(" ").map { it.toInt() };
    val books = reader.readLine().split(" ").mapIndexed { idx, value -> Book(idx, value.toInt()) }
    val libraries = mutableListOf<Library>()

    for (i in 0 until numberOfLibraries) {

        // Describing library
        val (_, timeToSignUp, booksEachDay) = reader.readLine().split(" ").map { it.toInt() };
        val libraryBooks = reader.readLine().split(" ").map { books[it.toInt()] }

        libraries.add(
            Library(
                id = i,
                timeToSignUp = timeToSignUp,
                numberOfBooksEachDay = booksEachDay,
                books = libraryBooks
            )
        )
    }
    println("input mapped")

    // SOLUTION


    // Output
    println("writing output")
    output.writeText("")

    FileOutputStream(output, true)
        .bufferedWriter()
        .use {
            out -> out.appendln("${libraries.size}")
            libraries.forEach {
                out.appendln("${it.id} ${it.books.size}")
                out.appendln("${it.books.joinToString { book -> book.id.toString() }}".replace(",", ""))
            }
        }




}
