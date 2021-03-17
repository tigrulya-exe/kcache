package ru.nsu.manasyan.kcache.core.resultbuilder.hit

import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.util.EtagResponseBuilder

class ResponseEntityCacheHitResultBuilder : KCacheHitResultBuilder<ResponseEntity<*>> {
    override fun build(currentETag: String): ResponseEntity<*> = EtagResponseBuilder.notModified(currentETag)
}