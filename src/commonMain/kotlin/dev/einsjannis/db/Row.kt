package dev.einsjannis.db

class Row(list: Map<Column<out Any>, Any>) : Map<Column<out Any>, Any> by list
