package ru.nsu.manasyan.kcache.core.etag.extractor

import org.springframework.http.HttpHeaders.IF_NONE_MATCH
import java.lang.reflect.Method
import javax.servlet.http.HttpServletRequest

class SpringContextRequestETagExtractor(
    private val request: HttpServletRequest
) : ETagExtractor {
    override fun extract(method: Method, methodArgs: Array<Any>): String? =
        request.getHeader(IF_NONE_MATCH)?.removeQuotes()
}