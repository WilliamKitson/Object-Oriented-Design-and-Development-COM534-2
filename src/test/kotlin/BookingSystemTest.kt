import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class BookingSystemTest {
    @Test
    fun testSignupValidStudent() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "student$i",
                "20CharactersOrMore2$i",
                false
            )

            val bookingSystem = BookingSystem()

            bookingSystem.signup(input.username, input.password)
            assert(accountsMatch(input, bookingSystem.users[i]))
        }
    }

    private fun clearAccounts() {
        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
            "org.sqlite.JDBC"
        )

        transaction {
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

            BookingSystem().signup(input.username, input.password)
            assert(accountsMatch(input, BookingSystem().users[i]))
        }
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

            BookingSystem().signup(input.username, input.password)
        }

        assert(BookingSystem().users.count() == 1)
    }

    @Test
    fun testShortPassword() {
        clearAccounts()

        BookingSystem().signup("student", "t00Sh0rt")
        assert(BookingSystem().users.isEmpty())
    }

    @Test
    fun testLowercasePassword() {
        clearAccounts()

        BookingSystem().signup("student", "20CHARACTERSORMORE20")
        assert(BookingSystem().users.isEmpty())
    }
}
