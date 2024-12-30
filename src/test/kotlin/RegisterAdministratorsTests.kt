import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class RegisterAdministratorsTests {
    private val connection = "jdbc:sqlite:4kitsw10_COM534_2_database_test.db"

    @Test
    fun testValidSignup() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "administrator$i",
                "20CharactersOrMore2$i",
                true
            )

            val bookingSystem = BookingSystem(connection)

            bookingSystem.signupAdministrator(input.username, input.password)
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
    fun testLoadAdministrators() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "administrator$i",
                "20CharactersOrMore2$i",
                true
            )

            BookingSystem(connection).signupAdministrator(input.username, input.password)
            assert(accountsMatch(input, BookingSystem(connection).users[i]))
        }
    }

    @Test
    fun testDuplicatedUsername() {
        clearAccounts()

        for (i in 0..5) {
            val input = User(
                "administrator",
                "20CharactersOrMore2$i",
                true
            )

            BookingSystem(connection).signupAdministrator(input.username, input.password)
        }

        assert(BookingSystem(connection).users.count() == 1)
    }

    @Test
    fun testShortPassword() {
        clearAccounts()

        BookingSystem(connection).signupAdministrator("administrator", "t00Sh0rt")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testLowercasePassword() {
        clearAccounts()

        BookingSystem(connection).signupAdministrator("administrator", "20CHARACTERSORMORE20")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testUppercasePassword() {
        clearAccounts()

        BookingSystem(connection).signupAdministrator("administrator", "20charactersormore20")
        assert(BookingSystem(connection).users.isEmpty())
    }

    @Test
    fun testNumeralsPassword() {
        clearAccounts()

        BookingSystem(connection).signupAdministrator("administrator", "charactersormorechar")
        assert(BookingSystem(connection).users.isEmpty())
    }
}
