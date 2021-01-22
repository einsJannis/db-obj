package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.Table
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ReadOnlyColumnDelegate<T : Any, TABLE : Table<TABLE>>(val value: T, val column: Column<T, TABLE>) : ReadOnlyProperty<TABLE, T> {

    override fun getValue(thisRef: TABLE, property: KProperty<*>): T = value

}
