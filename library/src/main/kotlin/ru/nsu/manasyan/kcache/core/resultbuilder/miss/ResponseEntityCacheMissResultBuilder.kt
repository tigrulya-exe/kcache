package ru.nsu.manasyan.kcache.core.resultbuilder.miss

import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.util.withEtag

class ResponseEntityCacheMissResultBuilder : KCacheMissResultBuilder<ResponseEntity<*>> {
    override fun build(functionResult: Any?, currentETag: String): ResponseEntity<*> =
        (functionResult as ResponseEntity<*>).withEtag(currentETag)
}