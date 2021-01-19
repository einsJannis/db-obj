package dev.einsjannis.db

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.reflect.KClass

sealed class DatabaseType<T : Any>(val kClass: KClass<out T>, val verify: (T) -> Boolean = { true }) {
    object Bit : DatabaseType<Boolean>(Boolean::class)
    object TinyInt : DatabaseType<Byte>(Byte::class)
    object SmallInt : DatabaseType<Short>(Short::class)
    object Int : DatabaseType<kotlin.Int>(kotlin.Int::class)
    object BigInt : DatabaseType<Long>(Long::class)
    object Decimal : DatabaseType<BigDecimal>(BigDecimal::class)
    object Numeric : DatabaseType<BigInteger>(BigInteger::class)
    object Real : DatabaseType<kotlin.Float>(kotlin.Float::class)
    object Float : DatabaseType<Double>(Double::class)
}