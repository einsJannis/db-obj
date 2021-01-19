package dev.einsjannis

class Cache<K, V>(val maxSize: Int) : MutableMap<K, V> by LinkedHashMap(maxSize) {
    fun removeEldestEntry(): Unit = TODO()
}