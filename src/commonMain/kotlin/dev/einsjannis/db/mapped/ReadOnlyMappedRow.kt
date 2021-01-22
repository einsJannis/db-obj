package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.Row
import dev.einsjannis.db.Table

abstract class ReadOnlyMappedRow<TABLE : Table<TABLE>>(val row: Row<TABLE>) {

    private val _fields: MutableList<ReadOnlyColumnDelegate<*, TABLE>> = mutableListOf()

    val fields: List<ReadOnlyColumnDelegate<*, TABLE>> get() = _fields

    protected fun <T : Any> map(column: Column<T, TABLE>) =
        ReadOnlyColumnDelegate(row[column]!! as T, column)

}
