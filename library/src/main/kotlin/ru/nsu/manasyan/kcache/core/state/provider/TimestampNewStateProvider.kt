package ru.nsu.manasyan.kcache.core.state.provider

import java.time.Instant
import java.time.format.DateTimeFormatter

class TimestampNewStateProvider : NewStateProvider {
    override fun provide(tableName: String): String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
}