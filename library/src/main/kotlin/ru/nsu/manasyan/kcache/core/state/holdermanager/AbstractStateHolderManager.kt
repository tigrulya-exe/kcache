package ru.nsu.manasyan.kcache.core.state.holdermanager

import ru.nsu.manasyan.kcache.core.state.holder.StateHolder

abstract class AbstractStateHolderManager : StateHolderManager {
    private val stateHolders: MutableMap<String, StateHolder> = mutableMapOf()

    override fun getStateHolder(stateHolderName: String): StateHolder? =
        stateHolders[stateHolderName]

    override fun getOrCreateStateHolder(stateHolderName: String): StateHolder =
        getStateHolder(stateHolderName) ?: createStateHolder(stateHolderName).also {
            stateHolders[stateHolderName] = it
        }

    protected abstract fun createStateHolder(stateHolderName: String): StateHolder
}