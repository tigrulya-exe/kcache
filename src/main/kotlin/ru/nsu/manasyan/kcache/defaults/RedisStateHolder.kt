package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.StateHolder

// TODO: implement
class RedisStateHolder: StateHolder {
    override fun getState(tableId: String): String? {
        TODO("Not yet implemented")
    }

    override fun setState(tableId: String, state: String) {
        TODO("Not yet implemented")
    }

    override fun removeState(tableId: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}