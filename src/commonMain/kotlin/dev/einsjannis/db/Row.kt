package dev.einsjannis.db

class Row<T : Table<T>> internal constructor(map: Map<Column<out Any, T>, Any>) : PartialRow<T>(map) {

    companion object {

        fun <T : Table<T>> build(table: T, builder: (row: Builder<T>) -> Unit): Row<T> {
            val builderResult = Builder<T>().also(builder).backing
            if (!table.columns.all { builderResult.containsKey(it) }) throw TODO()
            return Row(builderResult)
        }

    }

}
