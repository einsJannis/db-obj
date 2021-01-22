package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.Table
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RowBuilderColumnDelegate<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>) : ReadWriteProperty<MappedPartialRowBuilder<TABLE>, T> {

    override fun setValue(thisRef: MappedPartialRowBuilder<TABLE>, property: KProperty<*>, value: T) {
        thisRef.builder.backing[column] = value
    }

    override fun getValue(thisRef: MappedPartialRowBuilder<TABLE>, property: KProperty<*>): T =
        throw UnsupportedOperationException()

}
