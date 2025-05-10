import hyperskill.project.collections.Author
import hyperskill.project.collections.Book
import hyperskill.project.collections.Genre
import hyperskill.project.collections.Library
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class LibraryTest {
    private val author1 = Author(
        name = "Terry Pratchett",
        country = "United Kingdom",
        birthYear = 1945,
        deathYear = 2015
    )

    private val author2 = Author(
        name = "Neil Gaiman",
        country = "United Kingdom",
        birthYear = 1960,
        deathYear = null
    )

    private val author3 = Author(
        name = "George Raymond Richard Martin",
        country = "United States",
        birthYear = 1965,
        deathYear = null
    )

    private val book1 = Book(
        title = "Good Omens",
        language = "English",
        authors = listOf(author1, author2),
        genres = listOf(Genre.FANTASY, Genre.COMEDY, Genre.HORROR),
        year = 1990,
        amount = 1u
    )

    private val book2 = Book(
        title = "The Colour of Magic",
        language = "English",
        authors = listOf(author1),
        genres = listOf(Genre.FANTASY, Genre.COMEDY),
        year = 1983,
        amount = 1u
    )

    private val book3 = Book(
        title = "Die Farben der Magie",
        language = "German",
        authors = listOf(author1),
        genres = listOf(Genre.FANTASY, Genre.COMEDY),
        year = 2009,
        amount = 1u
    )

    private val book4 = Book(
        title = "Unknown Manual",
        language = "German",
        authors = null,
        genres = listOf(Genre.EDUCATION),
        year = null,
        amount = 1u
    )

    private val book5 = Book(
        title = "A Game of Thrones",
        language = "English",
        authors = listOf(author3),
        genres = listOf(Genre.FANTASY),
        year = 1996,
        amount = 1u
    )


    @Test
    fun `addBook should increase book list or stock`() {
        val lib = Library("Test", books = emptyList())
        lib.addBooks(book1)

        assertEquals(1, lib.books.size)
        assertEquals(1u, lib.books[0].amount)

        lib.addBooks(book1)
        assertEquals(1, lib.books.size)
        assertEquals(2u, lib.books[0].amount)

        lib.addBooks(book2)
        assertEquals(2, lib.books.size)
        assertEquals(1u, lib.books[1].amount)

        lib.addBooks(book2.copy(year = 2024))
        assertEquals(3, lib.books.size)
        assertEquals(1u, lib.books[1].amount)
        assertEquals(1u, lib.books[2].amount)
    }

    @Test
    fun `borrowBooks should reduce stock if available`() {
        val lib = Library("Test", books = listOf(book1.copy(), book2.copy(amount = 3u), book2.copy(year = 2024)))

        assertTrue(lib.borrowBooks(book1))
        assertEquals(3, lib.books.size)
        assertEquals(0u, lib.books[0].amount)

        assertFalse(lib.borrowBooks(book1))
        assertEquals(3, lib.books.size)
        assertEquals(0u, lib.books[0].amount)

        assertTrue(lib.borrowBooks(book2.copy(amount = 2u)))
        assertEquals(3, lib.books.size)
        assertEquals(1u, lib.books[1].amount)
        assertEquals(1u, lib.books[2].amount)

        assertFalse(lib.borrowBooks(book2.copy(amount = 2u)))
        assertEquals(3, lib.books.size)
        assertEquals(1u, lib.books[1].amount)
        assertEquals(1u, lib.books[2].amount)
    }

    @Test
    fun `returnBook should increase stock if book exists`() {
        val lib = Library("Test", books = listOf(book1.copy()))

        assertTrue(lib.returnBooks(book1))
        assertEquals(2u, lib.books[0].amount)

        assertFalse(lib.returnBooks(book2))
    }

    @Test
    fun `findBook should return book if book exists else return null`() {
        val book1Amount = 2u
        val lib = Library("Test", books = listOf(book1.copy(amount = book1Amount), book2.copy()))

        val book = lib.findBook(book1)
        assertNotNull(book)
        assertEquals(book1.title, book?.title)
        assertEquals(book1.language, book?.language)
        assertEquals(book1.authors, book?.authors)
        assertEquals(book1.genres, book?.genres)
        assertEquals(book1.year, book?.year)
        assertEquals(book1Amount, book?.amount)

        assertNull(lib.findBook(book3))

        assertNull(lib.findBook(book2.copy(year = 2009)))
        assertNull(lib.findBook(book2.copy(title = "The Light Fantastic")))
        assertNull(lib.findBook(book2.copy(authors = listOf(author2))))
        assertNull(lib.findBook(book2.copy(language = "German")))
    }

    @Test
    fun `findBooksByGenre should return list of books of selected genre or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(book1), lib.findBooksByGenre(Genre.HORROR))
        assertEquals(listOf(book1, book2, book3), lib.findBooksByGenre(Genre.COMEDY))
        assertEquals(emptyList<Book>(), lib.findBooksByGenre(Genre.DETECTIVE))
    }

    @Test
    fun `findBooksByLanguage should return list of books on specific language or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(book1, book2, book5), lib.findBooksByLanguage("English"))
        assertEquals(listOf(book3, book4), lib.findBooksByLanguage("German"))
        assertEquals(emptyList<Book>(), lib.findBooksByLanguage("French"))
    }

    @Test
    fun `findBooksByAuthorCountry should return list of books whose authors born in chosen country or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(book1, book2, book3), lib.findBooksByAuthorCountry("United Kingdom"))
        assertEquals(listOf(book5), lib.findBooksByAuthorCountry("United States"))
        assertEquals(emptyList<Book>(), lib.findBooksByAuthorCountry("Germany"))
    }

    @Test
    fun `findAuthorsByCountry should return list of authors born in chosen country or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(author1, author2), lib.findAuthorsByCountry("United Kingdom"))
        assertEquals(listOf(author3), lib.findAuthorsByCountry("United States"))
        assertEquals(emptyList<Author>(), lib.findAuthorsByCountry("Germany"))
    }

    @Test
    fun `findAuthorsByLanguage should return list of author with books on specific languages or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(author1, author2, author3), lib.findAuthorsByLanguage("English"))
        assertEquals(listOf(author1), lib.findAuthorsByLanguage("German"))
        assertEquals(emptyList<Author>(), lib.findAuthorsByLanguage("French"))
    }

    @Test
    fun `findBooksByTitleContains should return list of books with fitted titles or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(book2), lib.findBooksByTitleContains("magic"))
        assertEquals(listOf(book2), lib.findBooksByTitleContains("MAGIC"))
        assertEquals(emptyList<Book>(), lib.findBooksByTitleContains("MAGIC", false))
        assertEquals(listOf(book2, book5), lib.findBooksByTitleContains("of"))
        assertEquals(emptyList<Book>(), lib.findBooksByTitleContains("fantasy"))
    }

    @Test
    fun `findAuthorsByBirthDateRange should return list of authors including the range boundaries or empty list`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(listOf(author1, author2), lib.findAuthorsByBirthDateRange(1940, 1960))
        assertEquals(listOf(author2, author3), lib.findAuthorsByBirthDateRange(1960, 9999))
        assertEquals(emptyList<Author>(), lib.findAuthorsByBirthDateRange(2000, 9999))
    }

    @Test
    fun `findCoauthors should return list of other coauthors or empty list`() {
        val lib = Library(
            "Test",
            listOf(book1.copy(), book2.copy(), book1.copy(year = 2024), book3.copy(), book4.copy(), book5.copy())
        )

        assertEquals(listOf(author2), lib.findCoauthors(author1))
        assertEquals(listOf(author1), lib.findCoauthors(author2))
        assertEquals(emptyList<Author>(), lib.findCoauthors(author3))
    }

    @Test
    fun `findBooksWithoutAuthors should return list of books without authors (null or empty) or empty list`() {
        val lib = Library(
            "Test",
            listOf(
                book1.copy(),
                book2.copy(),
                book3.copy(),
                book4.copy(),
                book5.copy(),
                book5.copy(authors = emptyList())
            )
        )

        assertEquals(listOf(book4, book5.copy(authors = emptyList())), lib.findBooksWithoutAuthors())
        val lib2 = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book5.copy()))

        assertEquals(emptyList<Book>(), lib2.findBooksWithoutAuthors())
    }

    @Test
    fun `findBooksWithoutGenre should return list of books without genre (null or empty) or empty list`() {
        val lib = Library(
            "Test",
            listOf(
                book1.copy(),
                book2.copy(),
                book3.copy(),
                book4.copy(),
                book5.copy(),
                book1.copy(genres = emptyList()),
                book2.copy(genres = null)
            )
        )

        assertEquals(listOf(book1.copy(genres = emptyList()), book2.copy(genres = null)), lib.findBooksWithoutGenre())
        val lib2 = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(emptyList<Book>(), lib2.findBooksWithoutGenre())
    }

    @Test
    fun `findNotInStockBooks should return list of books with 0 amount or empty list`() {
        val lib = Library(
            "Test",
            listOf(
                book1.copy(),
                book2.copy(),
                book3.copy(),
                book4.copy(),
                book5.copy(),
                book1.copy(amount = 0u),
                book2.copy(amount = 0u)
            )
        )

        assertEquals(listOf(book1.copy(amount = 0u), book2.copy(amount = 0u)), lib.findNotInStockBooks())
        val lib2 = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(emptyList<Book>(), lib2.findNotInStockBooks())
    }

    @Test
    fun `groupBooksByLanguage should return map of languages and corresponding books`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(
            mapOf(
                "English" to listOf(book1, book2, book5),
                "German" to listOf(book3, book4)
            ), lib.groupBooksByLanguage()
        )
    }

    @Test
    fun `groupBooksByAuthor should return map of non null authors and corresponding books`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(
            mapOf(
                author1 to listOf(book1, book2, book3),
                author2 to listOf(book1),
                author3 to listOf(book5)
            ),
            lib.groupBooksByAuthor()
        )
    }

    @Test
    fun `groupBooksByGenre should return map of non null genres and corresponding books`() {
        val lib = Library(
            "Test",
            listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy(), book4.copy(genres = null))
        )

        assertEquals(
            mapOf(
                Genre.COMEDY to listOf(book1, book2, book3),
                Genre.HORROR to listOf(book1),
                Genre.FANTASY to listOf(book1, book2, book3, book5),
                Genre.EDUCATION to listOf(book4)
            ),
            lib.groupBooksByGenre()
        )
    }

    @Test
    fun `groupBooksByAuthorCountry should return map of non null author countries and corresponding books`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(
            mapOf(
                "United Kingdom" to listOf(book1, book2, book3),
                "United States" to listOf(book5),
            ),
            lib.groupBooksByAuthorCountry()
        )
    }

    @Test
    fun `groupBooksByDecade should return map of decade start and corresponding books`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(
            mapOf(
                1980 to listOf(book2),
                1990 to listOf(book1, book5),
                2000 to listOf(book3),
            ),
            lib.groupBooksByDecade()
        )
    }

    @Test
    fun `sortBooksByName should return list of books ordered by title by ascending or descending`() {
        val lib = Library("Test", listOf(book1.copy(), book2.copy(), book3.copy(), book4.copy(), book5.copy()))

        assertEquals(
            listOf(book5, book3, book1, book2, book4),
            lib.sortBooksByTitle()
        )

        assertEquals(
            listOf(book4, book2, book1, book3, book5),
            lib.sortBooksByTitle(false)
        )
    }

    @Test
    fun `sortBooksByAuthorName should return a list of books with authors sorted by author name, if a book has multiple authors, use the first author for ascending order, or the last author for descending order, using dictionary order`() {
        val newBook = book2.copy(authors = listOf(author1.copy(name = "Sir Terence David John Pratchett")))
        val lib = Library("Test", listOf(book1.copy(), newBook, book4.copy(), book5.copy()))

        assertEquals(
            listOf(book5, book1, newBook),
            lib.sortBooksByAuthorName()
        )

        assertEquals(
            listOf(book1, newBook, book5),
            lib.sortBooksByAuthorName(false)
        )
    }
}