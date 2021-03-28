package ru.nsu.manasyan.kcache.core.state.holdermanager

import org.redisson.api.RedissonClient
import ru.nsu.manasyan.kcache.core.state.holder.RedisStateHolder

class RedisStateHolderManager(
    private val redissonClient: RedissonClient
) : AbstractStateHolderManager() {
    override fun createStateHolder(stateHolderName: String) = RedisStateHolder(
        redissonClient,
        stateHolderName
    )
}