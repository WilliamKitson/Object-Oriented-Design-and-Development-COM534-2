class Booking(val user: User, val computer: Computer, val timeslot: Timeslot) {

    override fun toString(): String {
        return "Booking for computer ${computer.number}, $timeslot made by ${user.username}"
    }

    fun cancel() {
        computer.removeBooking(timeslot)
        user.bookings.remove(this)
    }
}