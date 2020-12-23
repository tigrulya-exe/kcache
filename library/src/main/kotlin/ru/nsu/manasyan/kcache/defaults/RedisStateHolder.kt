package ru.nsu.manasyan.kcache.defaults

import org.redisson.api.RedissonClient
import ru.nsu.manasyan.kcache.core.StateHolder

/**
 * DB tables' states storage in Redis
 */
class RedisStateHolder(
    private val redissonClient: RedissonClient
): StateHolder {
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