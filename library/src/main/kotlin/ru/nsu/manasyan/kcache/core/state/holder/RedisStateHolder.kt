package ru.nsu.manasyan.kcache.core.state.holder

import org.redisson.api.RedissonClient

/**
 * DB tables' states storage in Redis. Only for single instance usage.
 */
class RedisStateHolder(
    redissonClient: RedissonClient,
    stateHolderName: String
) : MapStateHolder(redissonClient.getMap(stateHolderName))