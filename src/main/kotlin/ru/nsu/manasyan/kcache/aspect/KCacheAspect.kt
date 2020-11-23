package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.api.ETagBuilder
import ru.nsu.manasyan.kcache.api.StateHolder
import ru.nsu.manasyan.kcache.util.LoggerProperty

@Aspect
class KCacheAspect(
    private val stateHolder: StateHolder,
    private val eTagBuilder: ETagBuilder
) {
    private val logger by LoggerProperty()

    @Around("@annotation(ru.nsu.manasyan.kcache.annotations.KCacheable)")
    fun wrapKCacheableControllerMethod(joinPoint: ProceedingJoinPoint): ResponseEntity<*> {
        logger.info("wrapKCacheableControllerMethod call")
        return ResponseEntity.ok("Hello, world")
    }
}