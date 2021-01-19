package dev.einsjannis.db

import dev.einsjannis.computeIfAbsent

class UpdateBuilder {

    private val _updates = mutableMapOf<Pair<Table, Condition>, MutableMap<Column<*>, Any>>()

    fun <T : Any> Table.update(column: Column<T>, value: T, where: Condition) {
        _updates.computeIfAbsent(Pair(this, where), ::mutableMapOf)[column] = value
    }

    fun <T : Any> Table.update(column: Column<T>, value: T, where: ConditionBuilderContext.() -> Condition) =
        update(column, value, condition(where))

}
