package dev.einsjannis.dbobjs

import dev.einsjannis.db.Column
import dev.einsjannis.db.DatabaseInterface
import dev.einsjannis.db.Table
import dev.einsjannis.db.DatabaseType
import dev.einsjannis.db.delete
import kotlin.reflect.KClass

abstract class DatabaseObject<P : Any>(val databaseInterface: DatabaseInterface, val serialName: String) {

    private var _table: Table? = null

    val table: Table get() = _table
        ?: Table(databaseInterface, serialName, _delegates.map { it.column }).also { _table = it }

    private var _primaryDelegate: ColumnDelegate<P, P>? = null

    val primaryColumn: Column<P> get() = _primaryDelegate?.column ?: throw TODO()
    val primaryValue: P get() = _primaryDelegate?.nonNullValue ?: throw TODO()

    internal val _delegates = mutableListOf<ColumnDelegate<P, *>>()

    private fun <T : Any> column(name: String, databaseType: DatabaseType<T>): Column<T> = Column(name, databaseType)

    private fun <T : Any> delegate(serialName: String, databaseType: DatabaseType<T>): ColumnDelegate<P, T> =
        ColumnDelegate(this, column(serialName, databaseType)).also { _delegates += it }

    protected fun bit(serialName: String) = delegate(serialName, DatabaseType.Bit)
    protected fun tinyint(serialName: String) = delegate(serialName, DatabaseType.TinyInt)
    protected fun smallint(serialName: String) = delegate(serialName, DatabaseType.SmallInt)
    protected fun int(serialName: String) = delegate(serialName, DatabaseType.Int)
    protected fun bigint(serialName: String) = delegate(serialName, DatabaseType.BigInt)
    protected fun decimal(serialName: String) = delegate(serialName, DatabaseType.Decimal)
    protected fun numeric(serialName: String) = delegate(serialName, DatabaseType.Numeric)
    protected fun real(serialName: String) = delegate(serialName, DatabaseType.Real)
    protected fun float(serialName: String) = delegate(serialName, DatabaseType.Float)
    protected fun date(serialName: String) = delegate(serialName, DatabaseType.Date)
    protected fun time(serialName: String) = delegate(serialName, DatabaseType.Time)
    protected fun datetime(serialName: String) = delegate(serialName, DatabaseType.DateTime)
    protected fun timestamp(serialName: String) = delegate(serialName, DatabaseType.Timestamp)
    protected fun year(serialName: String) = delegate(serialName, DatabaseType.Year)
    protected fun char(serialName: String, length: Int) = delegate(serialName, DatabaseType.Char(length))
    protected fun varchar(serialName: String, maxLength: Int) = delegate(serialName, DatabaseType.VarChar(maxLength))
    protected fun text(serialName: String) = delegate(serialName, DatabaseType.Text)
    protected fun nchar(serialName: String, length: Int) = delegate(serialName, DatabaseType.NChar(length))
    protected fun nvarchar(serialName: String, maxLength: Int) = delegate(serialName, DatabaseType.NVarChar(maxLength))
    protected fun ntext(serialName: String) = delegate(serialName, DatabaseType.NText)
    protected fun binary(serialName: String, length: Int) = delegate(serialName, DatabaseType.Binary(length))
    protected fun varbinary(serialName: String, maxLength: Int) = delegate(serialName, DatabaseType.VarBinary(maxLength))
    protected fun image(serialName: String) = delegate(serialName, DatabaseType.Image)
    protected fun clob(serialName: String) = delegate(serialName, DatabaseType.Clob)
    protected fun blob(serialName: String) = delegate(serialName, DatabaseType.Blob)
    protected fun xml(serialName: String) = delegate(serialName, DatabaseType.Xml)
    protected fun json(serialName: String) = delegate(serialName, DatabaseType.Json)
    protected fun <OP : Any, OT : DatabaseObject<OP>> `object`(serialName: String, companion: DatabaseObjectCompanion<OP, OT>) =
        delegate(serialName, DatabaseType.Object(companion))

    protected fun ColumnDelegate<P, P>.primary() {
        if (_primaryDelegate != null) throw TODO()
        _primaryDelegate = this
    }

    fun verify() = _delegates.all { it.verify() }

    fun delete() = table.delete { primaryColumn eq primaryValue }

}
