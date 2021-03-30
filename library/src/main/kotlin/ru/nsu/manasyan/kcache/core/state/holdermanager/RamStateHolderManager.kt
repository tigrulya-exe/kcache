package ru.nsu.manasyan.kcache.core.state.holdermanager

import ru.nsu.manasyan.kcache.core.state.holder.RamStateHolder

class RamStateHolderManager : AbstractStateHolderManager() {
    override fun createStateHolder(stateHolderName: String) = RamStateHolder()
}