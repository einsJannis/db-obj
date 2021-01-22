package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.PartialRow
import dev.einsjannis.db.Row
import dev.einsjannis.db.Table

abstract class MappedPartialRowBuilder<TABLE : Table<TABLE>> {

    internal val builder = PartialRow.Builder<TABLE>()

    private val _delegates: MutableList<RowBuilderColumnDelegate<*, TABLE>> = mutableListOf()

    val delegates: List<RowBuilderColumnDelegate<*, TABLE>> get() = _delegates.toList()

    open val row: PartialRow<TABLE> get() = PartialRow(builder.backing)

    protected fun <T : Any> map(column: Column<T, TABLE>): RowBuilderColumnDelegate<T, TABLE> =
        RowBuilderColumnDelegate(column).also { _delegates += it }

}
