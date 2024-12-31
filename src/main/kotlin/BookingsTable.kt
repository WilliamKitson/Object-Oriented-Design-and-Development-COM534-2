import org.jetbrains.exposed.sql.Table

object BookingsTable: Table() {
    val bookingId = integer("booking_id").autoIncrement()
    val username = text("username")
    val password = text("password")
    val number = text("number")
    val day = text("day")
    val time = integer("time")
    override val primaryKey = PrimaryKey(bookingId)
}
