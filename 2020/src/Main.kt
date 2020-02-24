import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import kotlin.math.pow

data class Book(val id: Int, val score: Int) {}

data class Library(
    val id: Int,
    var books: List<Book> = listOf<Book>(),
    val timeToSignUp: Int,
    val numberOfBooksEachDay: Int,
    var score: Double = 0.0
) {
    fun calculateScore(alreadyScanned: Set<Book> = setOf()) {
        this.books = this.books.minus(alreadyScanned);
        val workload = (this.books.size / this.numberOfBooksEachDay);
        val totalDays = this.timeToSignUp + workload
        this.score = (( (this.books.sumBy { it.score }.toDouble().pow(2) / totalDays) / timeToSignUp.toDouble().pow(2)));
    }
}

fun main() {
    for (ex in 'A' .. 'F') {
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

    val alreadyScanned = mutableSetOf<Book>()
    val result = mutableListOf<Library>()

    for (i in libraries.indices) {
        val lib = libraries[i]
        lib.calculateScore()
    }

    libraries.sortByDescending { it.score }


    for (i in libraries.indices) {
        val lib = libraries.first()
        libraries.removeAt(0);
        lib.books.forEach { alreadyScanned.add(it) }

        if (lib.books.isNotEmpty()) result.add(lib)

        for (l in libraries) {
            l.calculateScore(alreadyScanned = alreadyScanned)
        }

        libraries.sortByDescending { it.score }

        if (shouldBreak(result, numberOfDays)) break
    }

    result.sortByDescending { it.books.sumBy { book -> book.score } / ( it.timeToSignUp + (it.books.size / it.numberOfBooksEachDay)) }

    // Output
    println("score: ${calculateFinalScore(result)}")
    println("writing output...")
    output.writeText("")

    FileOutputStream(output, true)
        .bufferedWriter()
        .use { out ->
            out.appendln("${result.size}")
            result.forEach {
                out.appendln("${it.id} ${it.books.size}")
                out.appendln(it.books.joinToString { book -> book.id.toString() }.replace(",", ""))
            }
        }


}

fun shouldBreak(result: MutableList<Library>, numberOfDays: Int): Boolean {
    return result.sumBy { it.timeToSignUp  } > numberOfDays
}

fun calculateFinalScore(result: MutableList<Library>): Int{
    return result.sumBy { it.books.sumBy { book -> book.score  } }
}