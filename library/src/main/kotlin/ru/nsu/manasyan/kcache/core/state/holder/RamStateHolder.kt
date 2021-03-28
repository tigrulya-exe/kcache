package ru.nsu.manasyan.kcache.core.state.holder

import java.util.concurrent.ConcurrentHashMap

/**
 * DB tables' states storage in RAM. Only for single instance usage.
 */
class RamStateHolder : MapStateHolder(ConcurrentHashMap())
