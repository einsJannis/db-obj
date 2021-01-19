package dev.einsjannis.db

data class Column<T : Any>(val name: String, val databaseType: DatabaseType<T>)
