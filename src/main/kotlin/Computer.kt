class Computer(val number: String) {

    // Initialise the bookings to an array of 20 nulls (5 days * 4 timeslots)
    val weekBookings = arrayOfNulls<Booking>(20)

    fun makeBooking(day: String, time: Int, user: User) : Booking? {
        val timeslot = Timeslot(day, time)
        if(timeslot.index >= 0) {
            val b = Booking(user, this, timeslot)
            weekBookings[timeslot.index] = b
            user.addBooking(b)
            return b
        }
        return null
    }

    fun findBooking(day: String, time: Int) : Booking? {
        val timeslot = Timeslot(day, time)
        return if(timeslot.index >= 0) weekBookings[timeslot.index] else null
    }

    fun removeBooking(timeslot: Timeslot) {
        weekBookings[timeslot.index] = null
    }
}