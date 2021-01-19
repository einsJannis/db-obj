package dev.einsjannis.dbobjs

import dev.einsjannis.db.Column
import dev.einsjannis.db.UpdateBuilder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class ColumnDelegate<P : Any, T : Any>(val thisRef: DatabaseObject<P>, val column: Column<T>) : ReadWriteProperty<DatabaseObject<*>, T> {

    private var _value: T? = null

    var changed: Boolean = false
        private set

    internal val nonNullValue: T get() = _value ?: throw TODO()

    override fun setValue(thisRef: DatabaseObject<*>, property: KProperty<*>, value: T) {
        setValue(value)
    }

    override fun getValue(thisRef: DatabaseObject<*>, property: KProperty<*>): T {
        return nonNullValue
    }

    fun update(updateBuilder: UpdateBuilder) = updateBuilder.run {
        changed = false
        thisRef.table.update(column, nonNullValue) { thisRef.primaryColumn eq thisRef.primaryValue }
    }

    internal fun setValue(value: Any) {
        if (!column.databaseType.kClass.isInstance(value)) throw TODO()
        @Suppress("UNCHECKED_CAST")
        val v = value as T
        if (column.databaseType.verify(v)) throw TODO()
        if (!changed) changed = true
        _value = v
    }

    fun verify() = _value != null

}