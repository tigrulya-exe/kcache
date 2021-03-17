package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.handler.RequestStatesMappings
import java.lang.reflect.Method

/**
 * @return annotation of type T that is present on this method
 * or null if such annotation was not found
 */
inline fun <reified T> getAnnotationInstance(method: Method): T? {
    return method.annotations
        .filterIsInstance<T>()
        .ifEmpty {
            return null
        }[0]
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

fun RequestStatesMappings.getRequestStates(
    methodSignature: MethodSignature
): List<String> {
    return getRequestStates(methodSignature.getMethodName())
        ?: throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
}
