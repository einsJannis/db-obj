package dev.einsjannis.db

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.einsjannis.dbobjs.DatabaseObject
import dev.einsjannis.dbobjs.DatabaseObjectCompanion
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

sealed class DatabaseType<T : Any>(val kClass: KClass<out T>, val verify: (T) -> Boolean = { true }) {
    object Bit : DatabaseType<Boolean>(Boolean::class)
    object TinyInt : DatabaseType<Byte>(Byte::class)
    object SmallInt : DatabaseType<Short>(Short::class)
    object Int : DatabaseType<kotlin.Int>(kotlin.Int::class)
    object BigInt : DatabaseType<Long>(Long::class)
    object Decimal : DatabaseType<BigDecimal>(BigDecimal::class, {
        BigDecimal.fromInt(-10).pow(38)+BigDecimal.ONE < it &&
                it < BigDecimal.fromInt(10).pow(38)-BigDecimal.ONE
    })
    object Numeric : DatabaseType<BigInteger>(BigInteger::class, {
        BigInteger.fromInt(-10).pow(38)+BigInteger.ONE < it &&
                it < BigInteger.fromInt(10).pow(38)-BigInteger.ONE
    })
    object Real : DatabaseType<kotlin.Float>(kotlin.Float::class)
    object Float : DatabaseType<Double>(Double::class)
    object Date : DatabaseType<DatePeriod>(DatePeriod::class)
    object Time : DatabaseType<DateTimePeriod>(DateTimePeriod::class, {
        it.days == 0 && it.months == 0 && it.years == 0
    })
    object DateTime : DatabaseType<DateTimePeriod>(DateTimePeriod::class)
    object Timestamp : DatabaseType<Instant>(Instant::class)
    object Year : DatabaseType<kotlin.Int>(kotlin.Int::class)
    class Char(length: kotlin.Int) : DatabaseType<String>(String::class, { it.length == length })
    class VarChar(maxLength: kotlin.Int) : DatabaseType<String>(String::class, { it.length <= maxLength })
    object Text : DatabaseType<String>(String::class)
    class NChar(length: kotlin.Int) : DatabaseType<String>(String::class, { it.length == length })
    class NVarChar(maxLength: kotlin.Int) : DatabaseType<String>(String::class, { it.length <= maxLength })
    object NText : DatabaseType<String>(String::class)
    class Binary(length: kotlin.Int) : DatabaseType<ByteArray>(ByteArray::class, { it.size == length })
    class VarBinary(maxLength: kotlin.Int) : DatabaseType<ByteArray>(ByteArray::class, { it.size <= maxLength })
    object Image : DatabaseType<ByteArray>(ByteArray::class)
    object Clob : DatabaseType<String>(String::class)
    object Blob : DatabaseType<ByteArray>(ByteArray::class)
    object Xml : DatabaseType<String>(String::class)
    object Json : DatabaseType<String>(String::class)
    class Object<OP : Any, OT : DatabaseObject<OP>>(val databaseObjectCompanion: DatabaseObjectCompanion<OP, OT>) : DatabaseType<OP>(databaseObjectCompanion.primaryKClass)
}