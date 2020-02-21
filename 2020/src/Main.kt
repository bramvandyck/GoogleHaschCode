import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

data class Book(val id: Int, val score: Int) {}

data class Library(
    val id: Int,
    val books: List<Book> = mutableListOf(),
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
        println("${ex}: done!")
        println()
    }
}


fun solve(reader: BufferedReader, output: File) {
    println("mapping input...")
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
                books = libraryBooks.sortedByDescending { it.score }
            )
        )
    }


    println("calculating solution...")
    // SOLUTION
    libraries.sortByDescending { it.books.sumBy { book ->  book.score } / (it.timeToSignUp + (it.books.size / it.numberOfBooksEachDay)) }
    var remainingDays = numberOfDays;

    val scannedBooks = mutableSetOf<Book>()

    for (i in 0 until numberOfLibraries) {
        val lib = libraries.first();

        val validBooks = lib.books.minus(scannedBooks)
        lib.score = (validBooks.sumBy { it.score } / (lib.timeToSignUp + (validBooks.size / lib.numberOfBooksEachDay)))
        validBooks.forEach { scannedBooks.add(it) }

        remainingDays -= lib.timeToSignUp + (lib.books.size / lib.numberOfBooksEachDay)
        libraries.add(lib)
        libraries.removeAt(0);

        for(j in 0 until libraries.size){
            val lib = libraries[j];
            val validBooks = lib.books.minus(scannedBooks)
            val libMaxScore = validBooks.slice(0 .. min(remainingDays - lib.timeToSignUp, validBooks.size -1 )).sumBy { it.score }
            lib.score = libMaxScore  / (lib.timeToSignUp + (validBooks.size / lib.numberOfBooksEachDay))
        }
        libraries.sortByDescending { it.score }

    }



    // Output
    println("writing output...")
    output.writeText("")

    FileOutputStream(output, true)
        .bufferedWriter()
        .use { out ->
            out.appendln("${libraries.size}")
            libraries.forEach {
                out.appendln("${it.id} ${it.books.size}")
                out.appendln(it.books.joinToString { book -> book.id.toString() }.replace(",", ""))
            }
        }


}
