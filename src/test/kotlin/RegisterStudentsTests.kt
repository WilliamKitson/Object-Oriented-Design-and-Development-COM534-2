import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class RegisterStudentsTests {
    private val connection = "jdbc:sqlite:4kitsw10_COM534_2_database_test.db"

    @Test
    fun testValidSignup() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "student$i",
                "20CharactersOrMore2$i",
                false
            )

            val bookingSystem = BookingSystem(connection)

            bookingSystem.signupStudent(input.username, input.password)
            assert(accountsMatch(input, bookingSystem.users[i]))
        }
    }

    private fun clearAccounts() {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(AccountsTable)
            SchemaUtils.create(RoomsTable)
            AccountsTable.deleteAll()
        }
    }

    private fun accountsMatch(first: User, second: User): Boolean {
        if (first.username != second.username) {
            return false
        }

        if (first.password != second.password) {
            return false
        }

        if (first.isAdmin != second.isAdmin) {
            return false
        }

        return true
    }

    @Test
    fun testLoadStudent() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "student$i",
                "20CharactersOrMore2$i",
                false
            )

            BookingSystem(connection).signupStudent(input.username, input.password)
            assert(accountsMatch(input, BookingSystem(connection).users[i]))
        }
    }

    @Test
    fun testEmptyUsername() {
        clearAccounts()

        BookingSystem(connection).signupStudent("", "20CharactersOrMore20")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testEmptyUsernameError() {
        clearAccounts()

        val bookingSystem = BookingSystem(connection)

        bookingSystem.signupStudent(
            "",
            "20CharactersOrMore20"
        )

        assert(bookingSystem.getLastError() == "Error: you have not specified a username.")
    }

    @Test
    fun testDuplicatedUsername() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "student",
                "20CharactersOrMore2$i",
                false
            )

            BookingSystem(connection).signupStudent(input.username, input.password)
        }

        assert(BookingSystem(connection).users.count() == 1)
    }

    @Test
    fun testShortPassword() {
        clearAccounts()

        BookingSystem(connection).signupStudent("student", "t00Sh0rt")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testLowercasePassword() {
        clearAccounts()

        BookingSystem(connection).signupStudent("student", "20CHARACTERSORMORE20")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testUppercasePassword() {
        clearAccounts()

        BookingSystem(connection).signupStudent("student", "20charactersormore20")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testNumeralsPassword() {
        clearAccounts()

        BookingSystem(connection).signupStudent("student", "charactersormorechar")
        assert(BookingSystem(connection).users.isEmpty())
    }
}
