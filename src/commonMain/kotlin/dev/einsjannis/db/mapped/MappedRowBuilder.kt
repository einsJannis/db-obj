package dev.einsjannis.db.mapped

import dev.einsjannis.db.Row
import dev.einsjannis.db.Table

abstract class MappedRowBuilder<T : Table<T>>(val table: Table<T>) : MappedPartialRowBuilder<T>() {

    override val row: Row<T> get() {
        if (table.columns.all { builder.backing.containsKey(it) }) throw TODO()
        return Row(builder.backing)
    }

}
