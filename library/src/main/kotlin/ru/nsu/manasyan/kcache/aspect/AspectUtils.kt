package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import java.lang.reflect.Method

inline fun <reified T> getAnnotationInstance(method: Method): T = method.annotations.filterIsInstance<T>()[0]

fun RequestStatesMapper.getRequestStates(
    methodSignature: MethodSignature
): List<String> {
    return getRequestStates(methodSignature.getMethodName())
        ?: throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
}

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