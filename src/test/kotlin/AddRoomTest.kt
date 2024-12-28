import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AddRoomTest {
    @Test
    fun testAddRoom() {
        clearRooms()
        val bookingSystem = BookingSystem()

        for (i in 0..5) {
            val input = Room(
                i.toString(),
                i.toString(),
                i.toString(),
                i * 5
            )

            bookingSystem.addRoom(input)
            assert(roomsMatch(bookingSystem.rooms[i], input))
        }
    }

    private fun clearRooms() {
        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
            "org.sqlite.JDBC"
        )

        transaction {
            RoomsTable.deleteAll()
        }
    }

    private fun roomsMatch(first: Room, second: Room): Boolean {
        if (first.number != second.number) {
            return false
        }

        if (first.building != second.building) {
            return false
        }

        if (first.compType != second.compType) {
            return false
        }

        if (first.computers.size != second.computers.size) {
            return false
        }

        return true
    }

    @Test
    fun testLoadRoom() {
        clearRooms()

        for (i in 0..5) {
            val input = Room(
                i.toString(),
                i.toString(),
                i.toString(),
                i * 5
            )

            BookingSystem().addRoom(input)
            assert(roomsMatch(BookingSystem().rooms[i], input))
        }
    }
}
