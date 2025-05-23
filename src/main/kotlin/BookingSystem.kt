import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BookingSystem(private val connection: String) {
    val users = mutableListOf<User>()
    val rooms = mutableListOf<Room>()
    var currentUser: User? = null
    private var lastError = LastError.NoError

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

            SchemaUtils.create(BookingsTable)

            BookingsTable.selectAll().forEach {
                login(
                    it[BookingsTable.username],
                    it[BookingsTable.password],
                )

                bookRoomSuccessful(
                    it[BookingsTable.number],
                    it[BookingsTable.day],
                    it[BookingsTable.time],
                )

                logout()
            }
        }
    }

    fun signupStudent(username: String, password: String) {
        if (invalidUsername(username)) {
            lastError = LastError.UsernameEmpty
            return
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

    private fun invalidUsername(username: String): Boolean {
        if (username.isEmpty()) {
            return true
        }

        val user = users.firstOrNull {
            it.username == username
        }

        return user != null
    }

    fun signupAdministrator(username: String, password: String) {
        if (invalidUsername(username)) {
            lastError = LastError.UsernameEmpty
            return
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
        if (username.isEmpty()) {
            return LastError.UsernameEmpty
        }

        if (password.isEmpty()) {
            return LastError.PasswordEmpty
        }

        if (calculateUsernameUnregistered(username)) {
            return LastError.UsernameUnregistered
        }

        if (calculateIncorrectPassword(username, password)) {
            return LastError.PasswordIncorrect
        }

        return LastError.NoError
    }

    private fun calculateUsernameUnregistered(username: String): Boolean {
        val user = users.firstOrNull {
            it.username == username
        }

        return user == null
    }

    private fun calculateIncorrectPassword(username: String, password: String): Boolean {
        val user = users.firstOrNull {
            it.username == username
        }

        if (user == null) {
            return true
        }

        return user.password != password
    }

    fun getLastError(): String {
        return lastError.toString()
    }

    fun logout() {
        currentUser = null
    }

    fun addRoom(room: Room) {
        lastError = LastError.NoError
        val roomAuthenticator = RoomAuthenticator()

        if (!roomAuthenticator.authenticate(room, rooms)) {
            lastError = roomAuthenticator.getLastError(room, rooms)
            return
        }

        transaction {
            SchemaUtils.create(RoomsTable)

            RoomsTable.insert {
                it[RoomsTable.number] = room.number
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

    fun getAllBookingsForRoomAndDay(room: String, day: String) : List<Booking>? {
        val room = findRoomByNumber(room)
        if(room != null) {
            return room.getBookingsByDay(day)
        }
        return null
    }

    fun bookRoom(number: String, day: String, time: Int) : Boolean {
        if (bookRoomSuccessful(number, day, time)) {
            transaction {
                SchemaUtils.create(BookingsTable)

                BookingsTable.insert {
                    it[BookingsTable.username] = currentUser?.username.toString()
                    it[BookingsTable.password] = currentUser?.password.toString()
                    it[BookingsTable.number] = number
                    it[BookingsTable.day] = day
                    it[BookingsTable.time] = time
                }[BookingsTable.bookingId]
            }

            return true
        }

        return false
    }

    fun getUniqueRoomNumbers(): List<String> {
        val uniqueNumbers = mutableListOf<String>()

        for (i in rooms) {
            uniqueNumbers += i.number
        }

        return uniqueNumbers.distinct()
    }

    fun getUniqueRoomBuildings(): List<String> {
        val uniqueBuildings = mutableListOf<String>()

        for (i in rooms) {
            uniqueBuildings += i.building
        }

        return uniqueBuildings.distinct()
    }

    fun getUniqueRoomComputerTypes(): List<String> {
        val uniqueComputerTypes = mutableListOf<String>()

        for (i in rooms) {
            uniqueComputerTypes += i.compType
        }

        return uniqueComputerTypes.distinct()
    }

    fun cancel(booking: Booking) {
        println(booking)
    }

    private fun bookRoomSuccessful(number: String, day: String, time: Int): Boolean {
        val room = findRoomByNumber(number)
        if(room != null && currentUser != null) {
            return room.bookComputer(day, time, currentUser!!) != null
        }
        return false
    }
}