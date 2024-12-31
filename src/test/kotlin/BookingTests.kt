import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class BookingTests {
    private val connection = "jdbc:sqlite:4kitsw10_COM534_2_database_test.db"

    @Test
    fun testBooking() {
        clearBookings()

        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            bookingSystem.signupStudent(
                i.toString(),
                "2${i}CharactersOrMore2$i"
            )

            bookingSystem.addRoom(Room(
                i.toString(),
                i.toString(),
                i.toString(),
                i * 5
            ))
        }

        for (i in 1..5) {
            bookingSystem.login(
                i.toString(),
                "2${i}CharactersOrMore2$i"
            )

            bookingSystem.bookRoom(
                i.toString(),
                "Monday",
                9
            )

            val result = bookingSystem.getAllBookingsForRoomAndDay(i.toString(), "Monday")?.first().toString()
            assert(result == "Booking for computer $i-1, Monday at 9 made by $i")
        }
    }

    private fun clearBookings() {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(AccountsTable)
            SchemaUtils.create(RoomsTable)
            AccountsTable.deleteAll()
            RoomsTable.deleteAll()
        }
    }

    @Test
    fun testBookingLoaded() {
        clearBookings()

        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            bookingSystem.signupStudent(
                i.toString(),
                "2${i}CharactersOrMore2$i"
            )

            bookingSystem.addRoom(Room(
                i.toString(),
                i.toString(),
                i.toString(),
                i * 5
            ))
        }

        for (i in 1..5) {
            bookingSystem.login(
                i.toString(),
                "2${i}CharactersOrMore2$i"
            )

            bookingSystem.bookRoom(
                i.toString(),
                "Monday",
                9
            )

            val result = BookingSystem(connection).getAllBookingsForRoomAndDay(i.toString(), "Monday")?.first().toString()
            assert(result == "Booking for computer $i-1, Monday at 9 made by $i")
        }
    }
}
