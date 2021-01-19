package dev.einsjannis

fun <K, V> MutableMap<K, V>.computeIfAbsent(key: K, default: () -> V): V =
    this[key] ?: default().also { this[key] = it }
