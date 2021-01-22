package dev.einsjannis.db

open class PartialRow<TABLE : Table<TABLE>> internal constructor(private val backing: Map<Column<*, TABLE>, Any>) {

    @Suppress("UNCHECKED_CAST")
    operator fun <T : Any> get(column: Column<T, TABLE>): T? = backing[column] as T?

    fun <T : Any> contains(column: Column<T, TABLE>) = backing.containsKey(column)

    class Builder<TABLE : Table<TABLE>> {

        internal val backing = mutableMapOf<Column<*, TABLE>, Any>()

        operator fun <T : Any> set(column: Column<T, TABLE>, value: T): Unit {
            backing[column] = value
        }

    }

    companion object {

        fun <T : Table<T>> build(builder: (row: Builder<T>) -> Unit): PartialRow<T> = PartialRow(Builder<T>().also(builder).backing)

    }

}
