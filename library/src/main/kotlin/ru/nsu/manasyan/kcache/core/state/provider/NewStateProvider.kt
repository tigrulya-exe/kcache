package ru.nsu.manasyan.kcache.core.state.provider

fun interface NewStateProvider {
    fun provide(tableName: String): String
}