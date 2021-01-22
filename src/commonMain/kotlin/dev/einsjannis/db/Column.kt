package dev.einsjannis.db

data class Column<T : Any, TABLE : Table<TABLE>>(val name: String, val databaseType: DatabaseType<T>, val table: TABLE)
