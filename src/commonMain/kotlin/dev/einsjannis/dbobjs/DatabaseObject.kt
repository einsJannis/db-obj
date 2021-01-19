package dev.einsjannis.dbobjs

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.einsjannis.db.Column
import dev.einsjannis.db.DatabaseInterface
import dev.einsjannis.db.Table
import dev.einsjannis.db.DatabaseType
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant

abstract class DatabaseObject<P : Any>(val databaseInterface: DatabaseInterface, val serialName: String) {

    private var _table: Table? = null

    val table: Table get() = _table
        ?: Table(databaseInterface, serialName, _delegates.map { it.column }).also { _table = it }

    private var _primaryDelegate: ColumnDelegate<P>? = null

    val primaryColumn: Column<P> get() = _primaryDelegate?.column ?: throw TODO()
    val primaryValue: P get() = _primaryDelegate?.nonNullValue ?: throw TODO()

    private val _delegates = mutableListOf<ColumnDelegate<*>>()

    private fun <T : Any> column(name: String, databaseType: DatabaseType<T>): Column<T> = Column(name, databaseType)

    private inline fun <reified T : Any> delegate(serialName: String, databaseType: DatabaseType<T>): ColumnDelegate<T> =
        ColumnDelegate(this, column(serialName, databaseType)).also { _delegates += it }

    //TODO: delegate builders
    protected fun bit(serialName: String) = delegate(serialName, DatabaseType.Bit)
    protected fun tinyint(serialName: String) = delegate(serialName, DatabaseType.TinyInt)
    /*protected fun smallint(serialName: String) = delegate<Short>(serialName)
    protected fun int(serialName: String) = delegate<Int>(serialName)
    protected fun bigint(serialName: String) = delegate<Long>(serialName)
    protected fun decimal(serialName: String) =delegate<BigDecimal>(serialName) {
        BigDecimal.fromInt(-10).pow(38)+BigDecimal.ONE < it &&
                it < BigDecimal.fromInt(10).pow(38)-BigDecimal.ONE
    }
    protected fun numeric(serialName: String) = delegate<BigInteger>(serialName) {
        BigInteger.fromInt(-10).pow(38)+BigInteger.ONE < it &&
                it < BigInteger.fromInt(10).pow(38)-BigInteger.ONE
    }
    protected fun real(serialName: String) = delegate<Float>(serialName)
    protected fun float(serialName: String) = delegate<Double>(serialName)
    protected fun date(serialName: String) = delegate<DatePeriod>(serialName)
    protected fun time(serialName: String) = delegate<DateTimePeriod>(serialName) {
        it.days == 0 && it.months == 0 && it.years == 0
    }
    protected fun datetime(serialName: String) = delegate<DateTimePeriod>(serialName)
    protected fun timestamp(serialName: String) = delegate<Instant>(serialName)
    protected fun year(serialName: String) = delegate<DatePeriod>(serialName) {
        it.days == 0 && it.months == 0
    }
    protected fun char(serialName: String, length: Int) = delegate<String>(serialName) { it.length == length }
    protected fun varchar(serialName: String, length: Int) = delegate<String>(serialName) { it.length <= length }
    protected fun text(serialName: String) = delegate<String>(it.length)*/

    protected fun ColumnDelegate<P>.primary() {
        if (_primaryDelegate != null) throw TODO()
        _primaryDelegate = this
    }

}
