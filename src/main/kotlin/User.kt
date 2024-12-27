class User(val username: String, val password: String, val isAdmin: Boolean = false) {

    val bookings = mutableListOf<Booking>()


    fun addBooking(b: Booking) {
        bookings.add(b)
    }

    fun cancelBooking(index: Int) : Boolean {
        if(index in bookings.indices) {
            bookings[index].cancel()
            return true
        }
        return false
    }

    override fun toString() : String {
        return "$username (${if(isAdmin) "admin" else "not admin"})"
    }
}