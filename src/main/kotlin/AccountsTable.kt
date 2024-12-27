import org.jetbrains.exposed.sql.Table

object AccountsTable: Table("Accounts") {
    val accountId = integer("account_id").autoIncrement()
    val username = text("username")
    val password = text("password")
    val admin = bool("admin")
    override val primaryKey = PrimaryKey(accountId)
}
