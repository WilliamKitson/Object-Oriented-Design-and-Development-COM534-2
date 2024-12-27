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

            BookingSystem().signup(input.username, input.password)

            val output = BookingSystem().users[i]

            assert(output.username == input.username)
            assert(output.password == input.password)
            assert(output.isAdmin == input.isAdmin)
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
}
