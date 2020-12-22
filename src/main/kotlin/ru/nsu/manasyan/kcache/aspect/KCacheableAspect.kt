package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.core.KCacheable
import ru.nsu.manasyan.kcache.core.ETagBuilder
import ru.nsu.manasyan.kcache.util.EtagResponseBuilder
import ru.nsu.manasyan.kcache.util.LoggerProperty
import ru.nsu.manasyan.kcache.util.withEtag
import java.lang.reflect.Method

@Aspect
class KCacheableAspect(
    private val eTagBuilder: ETagBuilder
) {
    private val logger by LoggerProperty()

    /**
     * Функция вызываемая до методов контроллеров Спринга, помеченных аннотацией [KCacheable].
     * В случае наличия значение HTTP-хедера If-None-Match в аргументах метода и совпадения
     * его значения со значением вычисленного текущего ETag возвращается
     * [ResponseEntity] с кодом 304 и пустым телом. Оборачиваемый метод при этом **не вызывается**.
     *
     * В случае, если в аргументах метода не найдено значение HTTP-хедера If-None-Match, т.е.
     * аргумент, помеченный аннотацией [RequestHeader] со значением поля [RequestHeader.name]
     * равным [HttpHeaders.IF_NONE_MATCH], или данный аргумент не совпадает с текущим ETag,
     * то вызывается оборачиваемый метод и возвращается его результат ([ResponseEntity]), но
     * с проставленным заголовком ETag, равным текущему значению ETag.
     */
    @Around("@annotation(ru.nsu.manasyan.kcache.core.KCacheable)")
    fun wrapKCacheableControllerMethod(joinPoint: ProceedingJoinPoint): ResponseEntity<*> {
        val method = (joinPoint.signature as MethodSignature).method
        val currentETag = eTagBuilder.buildETag(getAnnotationInstance<KCacheable>(method).tables)
        val previousETag = getEtagFromMethodArgs(method, joinPoint.args)

        if (currentETag == previousETag) {
            logger.debug("Equal ETags: $currentETag, returning 304")
            return EtagResponseBuilder.notModified(currentETag)
        }

        logger.debug("Different ETags: Current [$currentETag] Previous[$previousETag]. Invoking method.")
        return getResponseEntity(joinPoint.proceed()).withEtag(currentETag)
    }

    /**
     * If functionReturn is [ResponseEntity] returns it,
     * otherwise wraps it in [ResponseEntity] with status code 200.
     */
    private fun getResponseEntity(functionReturn: Any): ResponseEntity<*> {
        return if (functionReturn is ResponseEntity<*>)
            functionReturn else ResponseEntity.ok(functionReturn)
    }

    /**
     * Получение значения HTTP-заголовка If-None-Match из аргументов метода контроллера спринга.
     * Т.е. аргумента, помеченного аннотацией [RequestHeader] со значением поля name равным [HttpHeaders.IF_NONE_MATCH]
     * @return Значения такого аргумента, null если такой не обнаружен.
     */
    private fun getEtagFromMethodArgs(method: Method, methodArgs: Array<Any>): String? {
        for ((currentParameter, parameterAnnotations)
        in method.parameterAnnotations.withIndex()) {
            for (parameterAnnotation in parameterAnnotations) {
                if (parameterAnnotation is RequestHeader
                    && parameterAnnotation.name == HttpHeaders.IF_NONE_MATCH
                ) {
                    return (methodArgs[currentParameter] as String).ifBlank {
                        logger.debug("Blank If-None-Match header value was found in method ${method.name}")
                        return null
                    }
                }
            }
        }

        logger.debug("If-None-Match header value wasn't found in method ${method.name}")
        return null
    }
}