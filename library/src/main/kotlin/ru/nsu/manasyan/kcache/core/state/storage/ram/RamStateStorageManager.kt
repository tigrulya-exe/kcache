package ru.nsu.manasyan.kcache.core.state.storage.ram

import ru.nsu.manasyan.kcache.core.state.storage.AbstractStateStorageManager

class RamStateStorageManager : AbstractStateStorageManager() {
    override fun createStateHolder(stateHolderName: String) = RamStateStorage()
}