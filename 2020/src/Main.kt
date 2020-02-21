import java.io.BufferedReader
import java.io.File
import java.lang.Integer.min

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
        val libraryBooks = reader.readLine().split(" ").map { books.first { b -> b.id == it.toInt() } }

        libraries.add(
            Library(
                id = i,
                timeToSignUp = timeToSignUp,
                numberOfBooksEachDay = booksEachDay,
                books = libraryBooks,
                score = (libraryBooks.sumBy { it.score }) /  ( timeToSignUp +  (libraryBooks.size / booksEachDay))
            )
        )
    }
    println("input mapped")

    libraries.sortByDescending { it.score }

    // Output
    println("writing output")
    output.writeText("")

    output.appendText("${libraries.size} \n");

    libraries.forEach {
        output.appendText("${it.id} ${it.books.size} \n")
        output.appendText("${it.books.joinToString { book -> book.id.toString() }} \n".replace(",", ""))
    }


}
