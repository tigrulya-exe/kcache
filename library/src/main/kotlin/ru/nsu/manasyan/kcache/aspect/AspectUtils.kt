package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
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

fun RequestHandlerMetadataContainer.getMetadata(
    methodSignature: MethodSignature
): RequestHandlerMetadata {
    return methodSignature.getMethodName().let { name ->
        getMetadata(name) ?: throw IllegalArgumentException("Wrong method name: $name")
    }
}
