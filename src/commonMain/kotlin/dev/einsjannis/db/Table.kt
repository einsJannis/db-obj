package dev.einsjannis.db

class Table(val databaseInterface: DatabaseInterface, val name: String, columns: List<Column<*>>): List<Column<*>> by columns {

    fun insert(row: Row) = databaseInterface.insert(row, this)

    fun select(columns: List<Column<*>>, where: Condition, limit: Int? = null): List<Row> =
        databaseInterface.select(columns, where, this, limit)

    fun update(row: PartialRow, where: Condition) = databaseInterface.update(row, where, this)

    fun delete(where: Condition) = databaseInterface.delete(where, this)

}
