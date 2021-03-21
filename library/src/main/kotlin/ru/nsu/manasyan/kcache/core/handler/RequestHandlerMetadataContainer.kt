package ru.nsu.manasyan.kcache.core.handler

/**
 * Storage of HTTP-request handler methods' names and corresponding methods' metadata mappings
 */
interface RequestHandlerMetadataContainer {
    companion object {
        const val GENERATED_METADATA_CLASS_NAME =
            "ru.nsu.manasyan.kcache.core.handler.GeneratedHandlerMetadataContainer"
    }

    fun getMetadata(handlerName: String): RequestHandlerMetadata?

    fun getAllMetadata(): Map<String, RequestHandlerMetadata>
}