package dev.einsjannis.db

interface Database {

    val tables: MutableList<Table<*>>

    fun <T : Table<T>> insert(row: Row<T>, table: T)

    fun <T : Table<T>> select(columns: List<Column<*, T>>, where: Condition, table: T, limit: Int? = null): List<PartialRow<T>>

    fun <T : Table<T>> select(where: Condition, table: T, limit: Int? = null): List<Row<T>>

    fun <T : Table<T>> update(row: PartialRow<T>, where: Condition, table: T)

    fun <T : Table<T>> delete(where: Condition, table: T)

    fun table(name: String): Table<*>

    fun createTables()

}
