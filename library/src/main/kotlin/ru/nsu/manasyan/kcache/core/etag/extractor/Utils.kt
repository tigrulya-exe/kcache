package ru.nsu.manasyan.kcache.core.etag.extractor

fun extractIfNoneMatchFromHeader(header: String?) = header?.replace("\"", "")