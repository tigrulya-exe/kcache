package ru.nsu.manasyan.kcache.core.state.holder

import ru.nsu.manasyan.kcache.util.LoggerProperty

open class MapStateHolder(
    val states: MutableMap<String, String>,
    private val wholeStateHolderKey: String = "",
    wholeStateHolderDefaultValue: String = "ETag-Default"
) : StateHolder {

    init {
        states[wholeStateHolderKey] = wholeStateHolderDefaultValue
    }

    private val logger by LoggerProperty()

    override fun getState(key: String) = states[key]

    override fun removeState(key: String) = states.remove(key) != null

    override fun clear() = states.clear()

    override fun setState(key: String, state: String) {
        if (wholeStateHolderKey == key) {
            setAll(state)
            return
        }
        setStateAction(key, state)
        setStateAction(wholeStateHolderKey, state)
    }

    override fun mergeState(key: String, default: String) =
        getState(key) ?: default.also {
            setState(key, default)
        }

    private fun setStateAction(key: String, state: String) {
        logger.debug("Update state of $key by $state")
        states[key] = state
    }

    protected open fun setAll(state: String) {
        for (entry in states) {
            entry.setValue(state)
        }
    }
}