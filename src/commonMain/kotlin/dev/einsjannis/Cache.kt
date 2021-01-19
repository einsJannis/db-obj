package dev.einsjannis

class Cache<K, V>(val size: Int) {

    data class Entry<V>(var usageId: Long, val value: V)

    private var _backing = LinkedHashMap<K, Entry<V>>(size)

    var used: Long = 0

    operator fun get(key: K): V? {
        _backing[key]?.let { it.usageId = (used++) }
        return _backing[key]?.value
    }

    operator fun set(key: K, value: V) {
        _backing[key] = Entry(used++, value)
        if (_backing.size > size) _backing.remove(_backing.entries.maxByOrNull { it.value.usageId }!!.key)
    }

}
