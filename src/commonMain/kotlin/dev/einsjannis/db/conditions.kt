package dev.einsjannis.db

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Condition

data class NotCondition(val a: Condition) : Condition()

data class AndCondition(val a: Condition, val b: Condition) : Condition()

data class OrCondition(val a: Condition, val b: Condition) : Condition()

data class EqualsCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val value: T) : Condition()

data class ContainsCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val values: List<T>) : Condition()

data class GreaterCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val value: T) : Condition()

data class GreaterEqualsCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val value: T) : Condition()

data class SmallerCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val value: T) : Condition()

data class SmallerEqualsCondition<T : Any, TABLE : Table<TABLE>>(val column: Column<T, TABLE>, val value: T) : Condition()

class ConditionBuilderContext<TABLE : Table<TABLE>> {

    fun not(condition: Condition) = NotCondition(condition)

    infix fun Condition.and(other: Condition) = AndCondition(this, other)

    infix fun Condition.or(other: Condition) = OrCondition(this, other)

    infix fun <T : Any> Column<T, TABLE>.eq(value: T): Condition = EqualsCondition(this, value)

    infix fun <T : Any> Column<T, TABLE>.`in`(values: List<T>): Condition = ContainsCondition(this, values)

    infix fun <T : Any> Column<T, TABLE>.greaterThen(value: T): Condition = GreaterCondition(this, value)

    infix fun <T : Any> Column<T, TABLE>.greaterEqualTo(value: T): Condition = GreaterEqualsCondition(this, value)

    infix fun <T : Any> Column<T, TABLE>.smallerThen(value: T): Condition = SmallerCondition(this, value)

    infix fun <T : Any> Column<T, TABLE>.smallerEqualTo(value: T): Condition = SmallerEqualsCondition(this, value)

}

fun <T : Table<T>> condition(block: ConditionBuilderContext<T>.() -> Condition): Condition {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return ConditionBuilderContext<T>().run(block)
}

fun <T : Table<T>> T.select(
    columns: List<Column<*, T>>,
    where: ConditionBuilderContext<T>.() -> Condition,
    limit: Int? = null
): List<PartialRow<T>> = this.select(columns, condition(where), limit)

fun <T : Table<T>> T.select(
    where: ConditionBuilderContext<T>.() -> Condition,
    limit: Int? = null
): List<Row<T>> = this.select(condition(where), limit)

fun <T : Table<T>> Database.select(
    columns: List<Column<*, T>>,
    where: ConditionBuilderContext<T>.() -> Condition,
    table: T,
    limit: Int? = null
) = this.select(columns, condition(where), table, limit)

fun <T : Table<T>> Database.select(
    where: ConditionBuilderContext<T>.() -> Condition,
    table: T,
    limit: Int? = null
) = this.select(condition(where), table, limit)

fun <T : Table<T>> T.update(row: PartialRow<T>, where: ConditionBuilderContext<T>.() -> Condition): Unit =
    this.update(row, condition(where))

fun <T : Table<T>> Database.update(row: PartialRow<T>, where: ConditionBuilderContext<T>.() -> Condition, table: T) =
    this.update(row, condition(where), table)

fun <T : Table<T>> T.delete(where: ConditionBuilderContext<T>.() -> Condition): Unit =
    this.delete(condition(where))

fun <T : Table<T>> Database.delete(where: ConditionBuilderContext<T>.() -> Condition, table: T): Unit =
    this.delete(condition(where), table)
