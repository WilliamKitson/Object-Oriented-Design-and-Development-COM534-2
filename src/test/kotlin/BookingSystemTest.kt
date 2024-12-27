import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class BookingSystemTest {
    @Test
    fun testSignupValidStudent() {
        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
            "org.sqlite.JDBC"
        )

        transaction {
            Accounts.deleteAll()
        }

        for (i in 0..5) {
            val user = User(
                "student$i",
                "20CharactersOrMore2$i",
                false
            )

            BookingSystem().signup(user.username, user.password)
            assert(BookingSystem().users[i] == user)
        }
    }
}
