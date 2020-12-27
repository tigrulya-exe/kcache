package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.ResponseEntity
import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import java.lang.reflect.Method

/**
 * @return annotation of type T that is present on this method
 * or null if such annotation not found
 */
inline fun <reified T> getAnnotationInstance(method: Method): T? {
    return method.annotations
        .filterIsInstance<T>()
        .ifEmpty {
            return null
        }[0]
}

/**
 * If functionReturn is [ResponseEntity] returns it,
 * otherwise wraps it in [ResponseEntity] with status code 200.
 */
fun getResponseEntity(functionReturn: Any): ResponseEntity<*> {
    return when (functionReturn) {
        is ResponseEntity<*> -> functionReturn
        else -> ResponseEntity.ok(functionReturn)
    }
}

/**
 * @return name of method (in package.class.method([arg: argType...]) format)
 */
fun MethodSignature.getMethodName(): String {
    return StringBuilder().apply {
        append(declaringTypeName)
        append(".")
        append(method.name)
        append("(")
        parameterTypes.forEach {
            append(it.name)
        }
        append(")")
    }.toString()
}

fun RequestStatesMapper.getRequestStates(
    methodSignature: MethodSignature
): List<String> {
    return getRequestStates(methodSignature.getMethodName())
        ?: throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
}
