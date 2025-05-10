package hyperskill.project.collections

import java.util.*

data class Book( // title + language + authors + year is unique combination
    val title: String,
    val language: String,
    val authors: List<Author>?,
    val genres: List<Genre>?,
    val year: Int?,
    var amount: UInt
)

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
        TODO("Not yet implemented")
    }

    override fun borrowBooks(book: Book): Boolean {
        TODO("Not yet implemented")
    }

    override fun returnBooks(book: Book): Boolean {
        TODO("Not yet implemented")
    }

    override fun findBook(book: Book): Book? {
        TODO("Not yet implemented")
    }

    override fun findBooksByGenre(genre: Genre): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findBooksByLanguage(language: String): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findBooksByAuthorCountry(country: String): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findAuthorsByCountry(country: String): List<Author> {
        TODO("Not yet implemented")
    }

    override fun findAuthorsByLanguage(language: String): List<Author> {
        TODO("Not yet implemented")
    }

    override fun findBooksByTitleContains(query: String, ignoreCase: Boolean): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findAuthorsByBirthDateRange(from: Int, to: Int): List<Author> {
        TODO("Not yet implemented")
    }

    override fun findCoauthors(author: Author): List<Author> {
        TODO("Not yet implemented")
    }

    override fun findBooksWithoutAuthors(): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findBooksWithoutGenre(): List<Book> {
        TODO("Not yet implemented")
    }

    override fun findNotInStockBooks(): List<Book> {
        TODO("Not yet implemented")
    }

    override fun groupBooksByLanguage(): Map<String, List<Book>> {
        TODO("Not yet implemented")
    }

    override fun groupBooksByAuthor(): Map<Author, List<Book>> {
        TODO("Not yet implemented")
    }

    override fun groupBooksByGenre(): Map<Genre, List<Book>> {
        TODO("Not yet implemented")
    }

    override fun groupBooksByAuthorCountry(): Map<String, List<Book>> {
        TODO("Not yet implemented")
    }

    override fun groupBooksByDecade(): Map<Int, List<Book>> {
        TODO("Not yet implemented")
    }

    override fun sortBooksByTitle(asc: Boolean): List<Book> {
        TODO("Not yet implemented")
    }

    override fun sortBooksByAuthorName(asc: Boolean): List<Book> {
        TODO("Not yet implemented")
    }
}