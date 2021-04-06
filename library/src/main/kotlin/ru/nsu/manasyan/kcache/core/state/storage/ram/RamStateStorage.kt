package ru.nsu.manasyan.kcache.core.state.storage.ram

import ru.nsu.manasyan.kcache.core.state.storage.MapStateStorage
import java.util.concurrent.ConcurrentHashMap

/**
 * DB tables' states storage in RAM. Only for single instance usage.
 */
class RamStateStorage : MapStateStorage(ConcurrentHashMap())
