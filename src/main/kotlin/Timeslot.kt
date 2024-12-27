class Timeslot(val day: String, val time: Int) {
    // The companion object of a class is used to create static methods. These are methods which
    // apply to the class as a whole, not a specific instance of the class. In other words,
    // they do not require access to the attributes which define a specific instance of the class.
    companion object {
        val daysOfWeek = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

        fun isValidTimeslot(timeslot: Int) : Boolean {
            return timeslot == 9 || timeslot == 11 || timeslot == 13 || timeslot == 15
        }

        fun getTimeslotIndex(day: String, time: Int) : Int {
            val dayNumber = daysOfWeek.indexOf(day)
            if(dayNumber >= 0 && isValidTimeslot(time)) {
                return dayNumber*4 + (time-9)/2
            }
            return -1
        }
    }

    val index = getTimeslotIndex(day, time)

    override fun toString(): String {
        return "$day at $time"
    }
}