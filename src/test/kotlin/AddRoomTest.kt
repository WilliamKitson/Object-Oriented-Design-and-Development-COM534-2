import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class AddRoomTest {
    private val connection = "jdbc:sqlite:4kitsw10_COM534_2_database_test.db"

    @Test
    fun testAddRoom() {
        clearRooms()
        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            val input = Room(
                i.toString(),
                i.toString(),
                i.toString(),
                i * 5
            )

            bookingSystem.addRoom(input)
            assert(bookingSystem.rooms[i] == input)
        }
    }

    private fun clearRooms() {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(AccountsTable)
            SchemaUtils.create(RoomsTable)
            RoomsTable.deleteAll()
        }
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

            BookingSystem(connection).addRoom(input)
            assert(BookingSystem(connection).rooms[i].toString() == input.toString())
        }
    }

    @Test
    fun testDuplicatedRoom() {
        clearRooms()

        for (i in 0..5) {
            val input = Room(
                "0",
                "building",
                i.toString(),
                i * 5
            )

            BookingSystem(connection).addRoom(input)
        }

        assert(BookingSystem(connection).rooms.size == 1)
    }

    @Test
    fun testDuplicatedError() {
        clearRooms()
        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            val input = Room(
                "0",
                "building",
                i.toString(),
                i * 5
            )

            bookingSystem.addRoom(input)
        }

        assert(bookingSystem.getLastError() == "Error: room already exists.")
    }

    @Test
    fun testDuplicatedResetError() {
        clearRooms()
        val bookingSystem = BookingSystem(connection)
        val input = Room(
            "0",
            "building",
            "0",
            0
        )

        bookingSystem.addRoom(input)
        bookingSystem.addRoom(input)

        bookingSystem.addRoom(Room(
            "1",
            "building",
            "0",
            0
        ))

        assert(bookingSystem.getLastError() == "")
    }

    @Test
    fun testUniqueRoomNumbers() {
        clearRooms()

        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            for (j in 0..5) {
                bookingSystem.addRoom(Room(
                    i.toString(),
                    j.toString(),
                    "",
                    0
                ))
            }
        }

        for (i in 0..5) {
            assert(bookingSystem.getUniqueRoomNumbers()[i] == i.toString())
        }
    }

    @Test
    fun testUniqueRoomBuildings() {
        clearRooms()

        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            for (j in 0..5) {
                bookingSystem.addRoom(Room(
                    j.toString(),
                    i.toString(),
                    "",
                    0
                ))
            }
        }

        for (i in 0..5) {
            assert(bookingSystem.getUniqueRoomBuildings()[i] == i.toString())
        }
    }

    @Test
    fun testUniqueRoomComputerTypes() {
        clearRooms()

        val bookingSystem = BookingSystem(connection)

        for (i in 0..5) {
            for (j in 0..5) {
                bookingSystem.addRoom(Room(
                    i.toString(),
                    j.toString(),
                    i.toString(),
                    0
                ))
            }
        }

        for (i in 0..5) {
            assert(bookingSystem.getUniqueRoomComputerTypes()[i] == i.toString())
        }
    }

    @Test
    fun testRoomNumberEmpty() {
        clearRooms()

        val bookingSystem = BookingSystem(connection)

        bookingSystem.addRoom(Room(
            "",
            "building",
            "PC",
            5
        ))

        assert(bookingSystem.rooms.isEmpty())
    }
}
