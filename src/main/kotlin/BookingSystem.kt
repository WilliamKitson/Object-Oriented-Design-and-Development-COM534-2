import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BookingSystem() {
    val users = mutableListOf<User>()
    val rooms = mutableListOf<Room>()
    var currentUser: User? = null

    init {
        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
            "org.sqlite.JDBC"
        )

        transaction {
            AccountsTable.selectAll().forEach {
                val user = User(
                    it[AccountsTable.username],
                    it[AccountsTable.password],
                    it[AccountsTable.admin]
                )

                addUser(user)
            }
        }
    }

    fun signup(username: String, password: String) {
        for (user in users) {
            if (user.username == username) {
                return
            }
        }

        if (!PasswordAuthenticator().valid(password)) {
            return
        }

        Database.connect(
            "jdbc:sqlite:4kitsw10_COM534_2_database.db",
            "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(AccountsTable)

            AccountsTable.insert {
                it[AccountsTable.username] = username
                it[AccountsTable.password] = password
                it[admin] = false
            }[AccountsTable.accountId]
        }

        addUser(User(username, password, false))
    }

    fun addUser(u: User) {
        users.add(u)
    }

    fun login(username: String, password: String) : User? {
        // singleOrNull() is a filter function which will either return a single instance of an object
        // which meets the criterion specified in the lambda, or null if no objects meet this criterion.
        currentUser = users.singleOrNull { it.username == username  && it.password == password }
        return currentUser
    }

    fun logout() {
        currentUser = null
    }

    fun addRoom(room: Room) {
        rooms.add(room)
    }

    fun findRoomByNumber(number: String) : Room? {
        return rooms.singleOrNull { it.number == number }
    }

    fun findRoomsByBuilding(building: String): List<Room> {
        return rooms.filter { it.building == building }
    }

    fun findRoomsByType(compType: String) : List<Room> {
        return rooms.filter { it.compType == compType }
    }

    fun getAllBookingsForRoomAndDay(room: String, day: String) : List<Booking>? {
        val room = findRoomByNumber(room)
        if(room != null) {
            return room.getBookingsByDay(day)
        }
        return null
    }

    fun bookRoom(number: String, day: String, time: Int) : Boolean {
        val room = findRoomByNumber(number)
        if(room != null && currentUser != null) {
            // !! indicates that we know currentUser will not be null in our case
            // We have to do this as in multithreaded applications, another thread (process) might update
            // currentUser to null after this "if" statement has been executed
            return room.bookComputer(day, time, currentUser!!) != null
        }
        return false
    }

    fun changeComputerType(roomNumber: String, type: String) : Boolean {
        val room = findRoomByNumber(roomNumber)
        if(room != null) {
            room.compType = type
            return true
        }
        return false
    }

}