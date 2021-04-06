package ru.nsu.manasyan.kcache.core.resultbuilder

import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.util.EtagResponseBuilder
import ru.nsu.manasyan.kcache.util.withEtag

open class ResponseEntityKCacheResultBuilder : KCacheResultBuilder<ResponseEntity<*>> {
    override fun onCacheMiss(result: Any?, currentETag: String)
        = (result as ResponseEntity<*>).withEtag(currentETag)

    override fun onCacheHit(currentETag: String) = EtagResponseBuilder.notModified(currentETag)
}