package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.TableWithPrimary

abstract class DatabaseObject<PRIMARY_TYPE : Any, TABLE : TableWithPrimary<PRIMARY_TYPE, TABLE>>(val primaryValue: PRIMARY_TYPE, val table: TABLE) {

    private val _delegates: MutableList<ColumnDelegate<PRIMARY_TYPE, *, TABLE>> = mutableListOf()

    val delegates: List<ColumnDelegate<PRIMARY_TYPE, *, TABLE>> get() = _delegates

    protected fun <T : Any> mapped(column: Column<T, TABLE>): ColumnDelegate<PRIMARY_TYPE, T, TABLE> =
        ColumnDelegate(this, column).also { _delegates += it }

}
