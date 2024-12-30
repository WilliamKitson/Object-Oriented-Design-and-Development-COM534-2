import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class LoginTests {
    private val connection = "jdbc:sqlite:4kitsw10_COM534_2_database_test.db"

    @Test
    fun testLoginSuccessful() {
        clearAccounts()

        val user = User(
            "student",
            "20CharactersOrMore20"
        )

        val bookingSystem = BookingSystem(connection)
        bookingSystem.signupStudent(user.username, user.password)

        bookingSystem.login(user.username, user.password)
        assert(bookingSystem.getLastError() == "")
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

    @Test
    fun testLoginUsernameError() {
        clearAccounts()

        val bookingSystem = BookingSystem(connection)

        bookingSystem.login("student", "password")
        assert(bookingSystem.getLastError() == "Error: username unregisted.")
    }
}