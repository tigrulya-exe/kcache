package ru.nsu.manasyan.kcache.core.state.holder

import org.redisson.api.RedissonClient

/**
 * DB tables' states storage in Redis. Only for single instance usage.
 */
class RedisStateHolder(
    private val redissonClient: RedissonClient
) : StateHolder {
    override fun getState(tableId: String): String? {
        return redissonClient.getBucket<String>(tableId).get()
    }

    override fun setState(tableId: String, state: String) {
        redissonClient.getBucket<String>(tableId).set(state)
    }

    override fun removeState(tableId: String): Boolean {
        return redissonClient.getBucket<String>(tableId).delete()
    }

    override fun clear() {
        redissonClient.keys.flushall()
    }
}