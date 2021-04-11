package ru.nsu.manasyan.kcache.core.state.storage

import ru.nsu.manasyan.kcache.util.LoggerProperty

open class MapStateStorage(
    val states: MutableMap<String, String>,
    wholeStateHolderDefaultValue: String = "ETag-Default"
) : StateStorage {

    init {
        states[StateStorage.WHOLE_TABLE_KEY] = wholeStateHolderDefaultValue
    }

    private val logger by LoggerProperty()

    override fun getState(key: String) = states[key]

    override fun removeState(key: String) = states.remove(key) != null

    override fun clear() = states.clear()

    override fun setState(key: String, state: String) {
        if (StateStorage.WHOLE_TABLE_KEY == key) {
            setAll(state)
            return
        }
        setStateAction(key, state)
        setStateAction(StateStorage.WHOLE_TABLE_KEY, state)
    }

    override fun mergeState(key: String, default: String) =
        getState(key) ?: default.also {
            setState(key, default)
        }

    private fun setStateAction(key: String, state: String) {
        logger.debug("Update state of '$key' with '$state'")
        states[key] = state
    }

    protected open fun setAll(state: String) {
        for (entry in states) {
            entry.setValue(state)
        }
    }
}