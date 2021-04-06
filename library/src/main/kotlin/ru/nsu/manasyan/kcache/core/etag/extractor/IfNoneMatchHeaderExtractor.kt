package ru.nsu.manasyan.kcache.core.etag.extractor

import java.lang.reflect.Method

// TODO: add unit tests to all extractors
fun interface IfNoneMatchHeaderExtractor {
    /**
     * @return value of IF_NONE_MATCH argument, null if such not found.
     */
    fun extract(method: Method, methodArgs: Array<Any>): String?
}

fun extractIfNoneMatchFromHeader(header: String?) = header?.replace("\"", "")