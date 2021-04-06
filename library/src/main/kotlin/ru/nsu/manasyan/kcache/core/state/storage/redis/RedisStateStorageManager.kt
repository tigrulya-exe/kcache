package ru.nsu.manasyan.kcache.core.state.storage.redis

import org.redisson.api.RedissonClient
import ru.nsu.manasyan.kcache.core.state.storage.AbstractStateStorageManager

class RedisStateStorageManager(
    private val redissonClient: RedissonClient
) : AbstractStateStorageManager() {
    override fun createStateHolder(stateHolderName: String) = RedisStateStorage(
        redissonClient,
        stateHolderName
    )
}