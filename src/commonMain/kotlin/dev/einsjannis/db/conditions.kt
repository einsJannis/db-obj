package dev.einsjannis.db

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Condition

data class NotCondition(val a: Condition) : Condition()

data class AndCondition(val a: Condition, val b: Condition) : Condition()

data class OrCondition(val a: Condition, val b: Condition) : Condition()

data class EqualsCondition<T : Any>(val column: Column<T>, val value: T) : Condition()

data class ContainsCondition<T : Any>(val column: Column<T>, val values: List<T>) : Condition()

data class GreaterCondition<T : Any>(val column: Column<T>, val value: T) : Condition()

data class GreaterEqualsCondition<T : Any>(val column: Column<T>, val value: T) : Condition()

data class SmallerCondition<T : Any>(val column: Column<T>, val value: T) : Condition()

data class SmallerEqualsCondition<T : Any>(val column: Column<T>, val value: T) : Condition()

object ConditionBuilderContext {

    fun not(condition: Condition) = NotCondition(condition)

    infix fun Condition.and(other: Condition) = AndCondition(this, other)

    infix fun Condition.or(other: Condition) = OrCondition(this, other)

    infix fun <T : Any> Column<T>.eq(value: T): Condition = EqualsCondition(this, value)

    infix fun <T : Any> Column<T>.`in`(values: List<T>): Condition = ContainsCondition(this, values)

    infix fun <T : Any> Column<T>.greaterThen(value: T): Condition = GreaterCondition(this, value)

    infix fun <T : Any> Column<T>.greaterEqualTo(value: T): Condition = GreaterEqualsCondition(this, value)

    infix fun <T : Any> Column<T>.smallerThen(value: T): Condition = SmallerCondition(this, value)

    infix fun <T : Any> Column<T>.smallerEqualTo(value: T): Condition = SmallerEqualsCondition(this, value)

}

fun condition(block: ConditionBuilderContext.() -> Condition): Condition {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return ConditionBuilderContext.run(block)
}

fun Table.select(
    columns: List<Column<*>>,
    where: ConditionBuilderContext.() -> Condition,
    limit: Int? = null
) = this.select(columns, condition(where), limit)

fun DatabaseInterface.select(
    columns: List<Column<*>>,
    where: ConditionBuilderContext.() -> Condition,
    table: Table,
    limit: Int? = null
) = this.select(columns, condition(where), table, limit)

fun Table.update(row: PartialRow, where: ConditionBuilderContext.() -> Condition) =
    this.update(row, condition(where))

fun DatabaseInterface.update(row: PartialRow, where: ConditionBuilderContext.() -> Condition, table: Table) =
    this.update(row, condition(where), table)

fun Table.delete(where: ConditionBuilderContext.() -> Condition) =
    this.delete(condition(where))

fun DatabaseInterface.delete(where: ConditionBuilderContext.() -> Condition, table: Table) =
    this.delete(condition(where), table)
