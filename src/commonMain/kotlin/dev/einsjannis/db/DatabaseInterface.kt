package dev.einsjannis.db

interface DatabaseInterface {

    val tables: List<Table>

    fun insert(row: Row, table: Table)

    fun select(columns: List<Column<*>>, where: Condition, table: Table, limit: Int? = null): List<Row>

    fun update(row: PartialRow, where: Condition, table: Table)

    fun delete(where: Condition, table: Table)

    fun table(name: String): Table

    fun createTables()

}