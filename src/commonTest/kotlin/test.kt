import dev.einsjannis.db.Column
import dev.einsjannis.db.Database
import dev.einsjannis.db.TableWithMappedClass
import dev.einsjannis.db.mapped.DatabaseObject

val database: Database get() = TODO()

class User(val id: Long) : DatabaseObject<Long, User.Companion>(id, Companion) {

    var email by mapped(Companion.email)

    var passwordHash by mapped(Companion.passwordHash)

    companion object : TableWithMappedClass<Long, Companion, User>(database, "users", ::User, 100) {

        val id = bigint("id")

        val email = varchar("email", 255)

        val passwordHash = char("password_hash", 255)

        override val primaryColumn: Column<Long, Companion> = id

    }

}
