package ru.nsu.manasyan.kcache.core.state.storage.redis

import org.redisson.api.RedissonClient
import ru.nsu.manasyan.kcache.core.state.storage.MapStateStorage

/**
 * DB tables' states storage in Redis. Only for single instance usage.
 */
class RedisStateStorage(
    redissonClient: RedissonClient,
    stateHolderName: String
) : MapStateStorage(redissonClient.getMap(stateHolderName))