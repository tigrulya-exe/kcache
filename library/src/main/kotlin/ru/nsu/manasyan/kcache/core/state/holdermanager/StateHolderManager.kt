package ru.nsu.manasyan.kcache.core.state.holdermanager

import ru.nsu.manasyan.kcache.core.state.holder.StateHolder

// TODO: add docs
interface StateHolderManager {
    fun getStateHolder(stateHolderName: String): StateHolder?

    fun getOrCreateStateHolder(stateHolderName: String): StateHolder
}