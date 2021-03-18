package ru.nsu.manasyan.kcache.core.handler

import ru.nsu.manasyan.kcache.exceptions.NotYetGeneratedException

/**
 * Storage of HTTP-request handler methods' names
 * and corresponding methods' metadata mappings located in RAM
 */
// TODO: add docs
class GeneratedHandlerMetadataContainer : RequestHandlerMetadataContainer {
    companion object {
        private const val EXCEPTION_MESSAGE =
            "Please run KCacheableProcessor annotation processor to generate this class"
    }

    private val metadataMap: Map<String, RequestHandlerMetadata> =
        throw NotYetGeneratedException(EXCEPTION_MESSAGE)

    override fun getMetadata(handlerName: String): RequestHandlerMetadata? =
        throw NotYetGeneratedException(EXCEPTION_MESSAGE)

    override fun getAllMetadata(): Map<String, RequestHandlerMetadata> =
        throw NotYetGeneratedException(EXCEPTION_MESSAGE)
}