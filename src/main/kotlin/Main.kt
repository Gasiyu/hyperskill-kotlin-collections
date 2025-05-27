package hyperskill.project.collections

import java.util.*

data class Book( // title + language + authors + year is unique combination
    val title: String,
    val language: String,
    val authors: List<Author>?,
    val genres: List<Genre>?,
    val year: Int?,
    var amount: UInt
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Book) {
            return title == other.title && language == other.language && authors == other.authors && genres == other.genres && year == other.year
        } else {
            false
        }
    }
}

data class Author( // name + country + birthYear + deathYear is unique combination
    val name: String,
    val country: String,
    val birthYear: Int,
    val deathYear: Int?
)

enum class Genre { ROMANCE, FANTASY, COMEDY, SCIENCE_FICTION, DETECTIVE, HISTORY, HORROR, EDUCATION, BIOGRAPHY }


interface BooksManagementSystem { // you can pass amount > 1
    fun addBooks(book: Book)
    fun borrowBooks(book: Book): Boolean // false if Book is not in stock or not enough books
    fun returnBooks(book: Book): Boolean // false if Book is not in library
}

interface SearchSystem {
    fun findBook(book: Book): Book? // search by title + language + authors + year

    fun findBooksByGenre(genre: Genre): List<Book>
    fun findBooksByLanguage(language: String): List<Book>
    fun findBooksByAuthorCountry(country: String): List<Book>
    fun findAuthorsByCountry(country: String): List<Author>
    fun findAuthorsByLanguage(language: String): List<Author>

    fun findBooksByTitleContains(query: String, ignoreCase: Boolean = true): List<Book>
    fun findAuthorsByBirthDateRange(from: Int, to: Int): List<Author>
    fun findCoauthors(author: Author): List<Author>

    fun findBooksWithoutAuthors(): List<Book>
    fun findBooksWithoutGenre(): List<Book>
    fun findNotInStockBooks(): List<Book>
}

interface GroupingSystem {
    fun groupBooksByLanguage(): Map<String, List<Book>>
    fun groupBooksByAuthor(): Map<Author, List<Book>>
    fun groupBooksByGenre(): Map<Genre, List<Book>>
    fun groupBooksByAuthorCountry(): Map<String, List<Book>>
    fun groupBooksByDecade(): Map<Int, List<Book>>
}

interface SortingSystem {
    fun sortBooksByTitle(asc: Boolean = true): List<Book>
    fun sortBooksByAuthorName(asc: Boolean = true): List<Book>
}

data class Library(val name: String, var books: List<Book>) : BooksManagementSystem, SearchSystem, GroupingSystem,
    SortingSystem {
    override fun addBooks(book: Book) {
        books.find { it == book }?.also { existingBook ->
            existingBook.amount += book.amount
        } ?: run {
            books = books.plus(book)
        }
    }

    override fun borrowBooks(book: Book): Boolean = books.find { it == book }?.let { existingBook ->
        if (existingBook.amount >= book.amount) {
            existingBook.amount -= book.amount
            true
        } else {
            false
        }
    } ?: false

    override fun returnBooks(book: Book): Boolean = books.find { it == book }?.let { existingBook ->
        existingBook.amount += book.amount
        true
    } ?: false

    override fun findBook(book: Book): Book? = books.find { it == book }

    override fun findBooksByGenre(genre: Genre): List<Book> = books.filter { it.genres?.contains(genre) == true }

    override fun findBooksByLanguage(language: String): List<Book> = books.filter { it.language == language }

    override fun findBooksByAuthorCountry(country: String): List<Book> = books.filter { book ->
        book.authors?.any { it.country == country } ?: false
    }

    override fun findAuthorsByCountry(country: String): List<Author> =
        books.flatMap { it.authors ?: emptyList() }.distinct().filter { it.country == country }

    override fun findAuthorsByLanguage(language: String): List<Author> =
        books.filter { it.language == language }.flatMap { it.authors ?: emptyList() }.distinct()

    override fun findBooksByTitleContains(query: String, ignoreCase: Boolean): List<Book> =
        books.filter { it.title.contains(query, ignoreCase) }

    override fun findAuthorsByBirthDateRange(from: Int, to: Int): List<Author> =
        books.flatMap { it.authors ?: emptyList() }.distinct().filter { it.birthYear in from..to }

    override fun findCoauthors(author: Author): List<Author> =
        books.find { it.authors?.contains(author) == true }?.authors?.filter { it != author } ?: emptyList()

    override fun findBooksWithoutAuthors(): List<Book> = books.filter { it.authors.isNullOrEmpty() }

    override fun findBooksWithoutGenre(): List<Book> = books.filter { it.genres == null || it.genres.isEmpty() }

    override fun findNotInStockBooks(): List<Book> = books.filter { it.amount == 0u }

    override fun groupBooksByLanguage(): Map<String, List<Book>> = books.groupBy { it.language }

    override fun groupBooksByAuthor(): Map<Author, List<Book>> = books.flatMap { book ->
        (book.authors ?: emptyList()).map { author -> author to book }
    }.groupBy({ it.first }, { it.second })

    override fun groupBooksByGenre(): Map<Genre, List<Book>> = books.flatMap { book ->
        (book.genres ?: emptyList()).map { genre -> genre to book }
    }.groupBy({ it.first }, { it.second })

    override fun groupBooksByAuthorCountry(): Map<String, List<Book>> =
        books.filter { it.authors?.isNotEmpty() == true }.groupBy { it.authors!!.first().country }

    override fun groupBooksByDecade(): Map<Int, List<Book>> =
        books.filter { it.year != null }.groupBy { book -> book.year!!.div(10).times(10) }

    override fun sortBooksByTitle(asc: Boolean): List<Book> = if (asc) {
        books.sortedBy { it.title }
    } else {
        books.sortedByDescending { it.title }
    }

    override fun sortBooksByAuthorName(asc: Boolean): List<Book> =
        books.filter { !it.authors.isNullOrEmpty() }.let { filteredBooks ->
            if (asc) {
                filteredBooks.sortedBy { if (it.authors!!.size > 1) it.authors.last().name else it.authors.first().name }
            } else {
                filteredBooks.sortedByDescending { it.authors!!.first().name }
            }
        }
}
