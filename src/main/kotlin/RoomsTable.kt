import org.jetbrains.exposed.sql.Table

object RoomsTable: Table("Rooms") {
    val roomId = integer("room_id").autoIncrement()
    val number = integer("number")
    val building = text("building")
    val computerType = text("computer_type")
    val nComputers = integer("computer_count")
    override val primaryKey = PrimaryKey(roomId)
}
