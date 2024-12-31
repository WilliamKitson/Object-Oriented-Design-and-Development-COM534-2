import domain.LastError
import domain.PasswordAuthenticator
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BookingSystem(private val connection: String) {
    val users = mutableListOf<User>()
    val rooms = mutableListOf<Room>()
    var currentUser: User? = null
    var lastError = LastError.NoError

    init {
        Database.connect(
            connection,
            "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(AccountsTable)

            AccountsTable.selectAll().forEach {
                val user = User(
                    it[AccountsTable.username],
                    it[AccountsTable.password],
                    it[AccountsTable.admin]
                )

                addUser(user)
            }

            SchemaUtils.create(RoomsTable)

            RoomsTable.selectAll().forEach {
                val room = Room(
                    it[RoomsTable.number].toString(),
                    it[RoomsTable.building],
                    it[RoomsTable.computerType],
                    it[RoomsTable.nComputers]
                )

                rooms.add(room)
            }
        }
    }

    fun signupStudent(username: String, password: String) {
        for (user in users) {
            if (user.username == username) {
                return
            }
        }

        val passwordAuthenticator = PasswordAuthenticator()

        if (!passwordAuthenticator.valid(password)) {
            lastError = passwordAuthenticator.getError(password)
            return
        }

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

    fun signupAdministrator(username: String, password: String) {
        for (user in users) {
            if (user.username == username) {
                return
            }
        }

        val passwordAuthenticator = PasswordAuthenticator()

        if (!passwordAuthenticator.valid(password)) {
            lastError = passwordAuthenticator.getError(password)
            return
        }

        transaction {
            SchemaUtils.create(AccountsTable)

            AccountsTable.insert {
                it[AccountsTable.username] = username
                it[AccountsTable.password] = password
                it[admin] = true
            }[AccountsTable.accountId]
        }

        addUser(User(username, password, true))
    }

    fun addUser(u: User) {
        users.add(u)
    }

    fun login(username: String, password: String) : User? {
        lastError = calculateLoginError(username, password)
        currentUser = users.singleOrNull { it.username == username  && it.password == password }
        return currentUser
    }

    private fun calculateLoginError(username: String, password: String): LastError {
        lastError = LastError.UsernameUnregistered

        for (user in users) {
            if (user.username == username) {
                if (user.password != password) {
                    return LastError.PasswordIncorrect
                }

                return LastError.NoError
            }
        }

        return LastError.UsernameUnregistered
    }

    fun getLastError(): String {
        return lastError.toString()
    }

    fun logout() {
        currentUser = null
    }

    fun addRoom(room: Room) {
        for (i in rooms) {
            if (i.number == room.number) {
                if (i.building == room.building) {
                    lastError = LastError.RoomAlreadyExists
                    return
                }
            }
        }

        transaction {
            SchemaUtils.create(RoomsTable)

            RoomsTable.insert {
                it[RoomsTable.number] = room.number.toInt()
                it[RoomsTable.building] = room.building
                it[RoomsTable.computerType] = room.compType
                it[RoomsTable.nComputers] = room.computers.size
            }[RoomsTable.roomId]
        }

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