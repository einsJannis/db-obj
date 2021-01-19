package dev.einsjannis.db

class PartialRow(map: Map<Column<*>, Any>) : Map<Column<*>, Any> by map
