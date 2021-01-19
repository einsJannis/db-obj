package dev.einsjannis.db

import kotlin.reflect.KClass

data class Column<out T : Any>(val name: String, val kClass: KClass<out T>) {

}
