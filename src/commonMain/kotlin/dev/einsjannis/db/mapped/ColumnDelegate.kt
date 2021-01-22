package dev.einsjannis.db.mapped

import dev.einsjannis.db.Column
import dev.einsjannis.db.Row
import dev.einsjannis.db.TableWithPrimary
import dev.einsjannis.db.UpdateBuilder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ColumnDelegate<PRIMARY_TYPE : Any, T : Any, TABLE : TableWithPrimary<PRIMARY_TYPE, TABLE>>(val databaseObject: DatabaseObject<PRIMARY_TYPE, TABLE>, val column: Column<T, TABLE>) : ReadWriteProperty<DatabaseObject<PRIMARY_TYPE, TABLE>, T> {

    private var _value: T? = null

    var value: T
        get() = _value ?: throw TODO()
        set(value) { updated; _value = value }

    private var updated: Boolean = false

    override fun setValue(thisRef: DatabaseObject<PRIMARY_TYPE, TABLE>, property: KProperty<*>, value: T) {
        updated = true
        _value = value
    }

    override fun getValue(thisRef: DatabaseObject<PRIMARY_TYPE, TABLE>, property: KProperty<*>): T =
        _value ?: throw TODO()

    val isValid get() = _value?.let { column.databaseType.verify(it) } ?: false

    internal fun set(row: Row<TABLE>) { _value = row[column] }

    fun update(updateBuilder: UpdateBuilder) = if (updated) updateBuilder.run {
        databaseObject.table.update(column, value, { databaseObject.table.primaryColumn eq databaseObject.primaryValue })
    } else Unit

}
