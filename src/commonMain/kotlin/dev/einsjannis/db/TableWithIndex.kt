package dev.einsjannis.db

abstract class TableWithIndex<INDEX_TYPE : Any, TABLE : TableWithIndex<INDEX_TYPE, TABLE>>(database: Database, name: String) : Table<TABLE>(database, name) {

    @Suppress("UNCHECKED_CAST")
    fun select(columns: List<Column<*, TABLE>>, where: INDEX_TYPE, limit: Int? = null): List<PartialRow<TABLE>> =
        this.database.select(columns, { this@TableWithIndex.indexColumn eq where }, this as TABLE, limit)

    @Suppress("UNCHECKED_CAST")
    fun select(where: INDEX_TYPE, limit: Int? = null): List<Row<TABLE>> =
        this.database.select({ this@TableWithIndex.indexColumn eq where }, this as TABLE, limit)

    @Suppress("UNCHECKED_CAST")
    fun update(partialRow: PartialRow<TABLE>, where: INDEX_TYPE): Unit =
        this.database.update(partialRow, { this@TableWithIndex.indexColumn eq where }, this as TABLE)

    @Suppress("UNCHECKED_CAST")
    fun delete(where: INDEX_TYPE): Unit =
        this.database.delete({ this@TableWithIndex.indexColumn eq where }, this as TABLE)

    abstract val indexColumn: Column<INDEX_TYPE, TABLE>

}
