package dev.einsjannis.dbobjs

import dev.einsjannis.db.Column
import dev.einsjannis.db.DatabaseInterface
import dev.einsjannis.db.Table
import kotlin.reflect.KProperty1

abstract class DatabaseObject<P : Any>(val databaseInterface: DatabaseInterface, val serialName: String) {

    private var _table: Table? = null

    val table: Table get() = _table
        ?: Table(databaseInterface, serialName, _delegates.map { it.column }).also { _table = it }

    private var _primaryColumn: Column<P>? = null
    val primaryColumn: Column<P> get() = _primaryColumn ?: throw TODO()

    private var _primaryField: KProperty1<DatabaseObject<P>, P>? = null
    val primaryValue: P get() = _primaryField?.let { it.get(this) } ?: throw TODO()

    private val _delegates = mutableListOf<ColumnDelegate<*>>()

    private inline fun <reified T : Any> column(name: String): Column<T> = Column(name, T::class)

    private inline fun <reified T : Any> delegate(serialName: String): ColumnDelegate<T> =
        ColumnDelegate(this, column<T>(serialName)).also { _delegates += it }

    //TODO: delegate builders

    protected fun ColumnDelegate<P>.primary(field: KProperty1<DatabaseObject<P>, P>) {
        if (_primaryColumn != null || _primaryField != null) throw TODO()
        _primaryColumn = this.column
        _primaryField = field
    }

}
