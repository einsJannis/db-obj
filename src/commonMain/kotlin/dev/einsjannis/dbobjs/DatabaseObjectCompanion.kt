package dev.einsjannis.dbobjs

import dev.einsjannis.Cache
import dev.einsjannis.db.Row
import dev.einsjannis.db.select
import kotlin.reflect.KClass

abstract class DatabaseObjectCompanion<P : Any, T : DatabaseObject<P>>(val kClass: KClass<T>, val theConstructor: () -> T, cacheSize: Int = 255) {

    private val cached = Cache<P, T>(cacheSize)

    fun load(identifier: P): T? {
        cached[identifier]?.let { return@load it }
        val databaseObject = theConstructor()
        val columns = databaseObject._delegates.map { it.column }
        val row = databaseObject.table.select(columns, { databaseObject.primaryColumn eq identifier }).firstOrNull() ?: return null
        databaseObject._delegates.forEach {
            it.setValue(row[it.column] ?: throw TODO())
        }
        cached[identifier] = databaseObject
        return databaseObject
    }

    fun new(builder: T.() -> Unit): T {
        val databaseObject = theConstructor()
        if (databaseObject.also(builder).verify()) throw TODO()
        val map =
            databaseObject._delegates.map { it.column }.zip(databaseObject._delegates.map { it.nonNullValue }).toMap()
        databaseObject.table.insert(Row(map))
        cached[databaseObject.primaryValue] = databaseObject
        return databaseObject
    }

}