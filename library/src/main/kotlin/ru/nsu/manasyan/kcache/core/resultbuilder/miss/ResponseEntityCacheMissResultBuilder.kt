package ru.nsu.manasyan.kcache.core.resultbuilder.miss

import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.util.EtagResponseBuilder
import ru.nsu.manasyan.kcache.util.withEtag

class ResponseEntityCacheMissResultBuilder : KCacheMissResultBuilder<ResponseEntity<*>> {
    override fun build(functionResult: Any?, currentETag: String): ResponseEntity<*> = wrapInResponseEntity(
        functionResult
    ).withEtag(currentETag)

    /**
     * If functionReturn is [ResponseEntity] returns it,
     * otherwise wraps it in [ResponseEntity] with status code 200.
     */
    private fun wrapInResponseEntity(functionResult: Any?): ResponseEntity<*> {
        return when (functionResult) {
            is ResponseEntity<*> -> functionResult
            else -> functionResult?.let {
                ResponseEntity.ok(it)
            } ?: EtagResponseBuilder.empty()
        }
    }
}