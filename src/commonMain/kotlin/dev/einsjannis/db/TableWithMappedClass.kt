package dev.einsjannis.db

import dev.einsjannis.Cache
import dev.einsjannis.db.mapped.DatabaseObject

abstract class TableWithMappedClass<PRIMARY_TYPE : Any, TABLE : TableWithMappedClass<PRIMARY_TYPE, TABLE, MAPPED>, MAPPED : DatabaseObject<PRIMARY_TYPE, TABLE>>(database: Database, name: String, val theConstructor: (PRIMARY_TYPE) -> MAPPED, cacheSize: Int) : TableWithPrimary<PRIMARY_TYPE, TABLE>(database, name) {

    val cache: Cache<PRIMARY_TYPE, MAPPED> = Cache(cacheSize)

    @Suppress("UNCHECKED_CAST")
    operator fun get(primaryValue: PRIMARY_TYPE): MAPPED {
        val mapped = theConstructor(primaryValue)
        val row = this.select(primaryValue)
        mapped.delegates.forEach {
            it.set(row)
        }
        cache[primaryValue] = mapped
        return mapped
    }

    fun create(primaryValue: PRIMARY_TYPE, builder: MAPPED.() -> Unit): MAPPED =
        theConstructor(primaryValue).also(builder).also { cache[primaryValue] = it }

}
