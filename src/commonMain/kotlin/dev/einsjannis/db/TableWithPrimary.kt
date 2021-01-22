package dev.einsjannis.db

abstract class TableWithPrimary<PRIMARY_TYPE : Any, TABLE : TableWithPrimary<PRIMARY_TYPE, TABLE>>(database: Database, name: String) : Table<TABLE>(database, name) {

    @Suppress("UNCHECKED_CAST")
    fun select(columns: List<Column<*, TABLE>>, where: PRIMARY_TYPE): PartialRow<TABLE> =
        this.database.select(columns, { this@TableWithPrimary.primaryColumn eq where }, this as TABLE, 1).first()

    @Suppress("UNCHECKED_CAST")
    fun select(where: PRIMARY_TYPE): Row<TABLE> =
        this.database.select({ this@TableWithPrimary.primaryColumn eq where }, this as TABLE, 1).first()

    @Suppress("UNCHECKED_CAST")
    fun update(partialRow: PartialRow<TABLE>, where: PRIMARY_TYPE): Unit =
        this.database.update(partialRow, { this@TableWithPrimary.primaryColumn eq where }, this as TABLE)

    @Suppress("UNCHECKED_CAST")
    fun delete(where: PRIMARY_TYPE): Unit =
        this.database.delete({ this@TableWithPrimary.primaryColumn eq where }, this as TABLE)

    abstract val primaryColumn: Column<PRIMARY_TYPE, TABLE>

}
