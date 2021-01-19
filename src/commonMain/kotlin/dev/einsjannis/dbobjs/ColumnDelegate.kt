package dev.einsjannis.dbobjs

import dev.einsjannis.db.Column
import dev.einsjannis.db.UpdateBuilder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class ColumnDelegate<T : Any>(val thisRef: DatabaseObject<*>, val column: Column<T>) : ReadWriteProperty<DatabaseObject<*>, T> {

    private var _value: T? = null

    var changed: Boolean = false
        private set

    private val nonNullValue: T get() = _value ?: throw TODO()

    override fun setValue(thisRef: DatabaseObject<*>, property: KProperty<*>, value: T) {
        if (!changed) changed = true
        _value = value
    }

    override fun getValue(thisRef: DatabaseObject<*>, property: KProperty<*>): T {
        return nonNullValue
    }

    fun update(updateBuilder: UpdateBuilder) = updateBuilder.run {
        changed = false
        thisRef.table.update(column, nonNullValue) { thisRef.primaryColumn eq thisRef.primaryValue }
    }

}