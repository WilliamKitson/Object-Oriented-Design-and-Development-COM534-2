class Room(val number: String, val building: String, var compType: String, nComputers: Int) {
    val computers = Array(nComputers) {
        // This lambda is used to initialise each member of the array with a value, in this example
        // a computer numbered appropriately. "it" in the lambda is the current array index.
        Computer("$number-${it+1}")
    }

    fun bookComputer(day: String, time: Int, user: User) : Booking? {
        val computer = findFreeComputer(day, time)
        if(computer != null) {
            return computer.makeBooking(day, time, user)
        }
        return null
    }

    fun findFreeComputer (day: String, time: Int) : Computer? {
        return computers.find { it.findBooking(day, time) == null }
    }

    fun getBookingsByDay (day: String) : List<Booking> {
        val bookings = mutableListOf<Booking>()
        computers.forEach {
            bookings += it.weekBookings.filterNotNull().filter { booking -> booking.timeslot.day == day }
        }
        return bookings
    }

    override fun toString(): String {
        return "Room $number in building $building with ${computers.size} $compType machines"
    }
}