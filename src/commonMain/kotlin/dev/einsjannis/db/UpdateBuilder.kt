package dev.einsjannis.db

import dev.einsjannis.computeIfAbsent

class UpdateBuilder {

    private val _updates = mutableMapOf<Pair<Table<*>, Condition>, MutableMap<Column<*, *>, Any>>()

    fun <T : Any, TABLE : Table<TABLE>> TABLE.update(column: Column<T, TABLE>, value: T, where: Condition) {
        _updates.computeIfAbsent(Pair(this, where), ::mutableMapOf)[column] = value
    }

    fun <T : Any, TABLE : Table<TABLE>> TABLE.update(column: Column<T, TABLE>, value: T, where: ConditionBuilderContext<TABLE>.() -> Condition) =
        update(column, value, condition(where))

    operator fun invoke() = _updates.forEach { (pair, changes) ->
        val (table, condition) = pair
        //TODO: table.update(PartialRow(changes), condition)
    }

}
