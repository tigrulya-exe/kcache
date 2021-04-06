package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.reflect.MethodSignature

/**
 * @return name of method (in package.class.method([arg: argType...]) format)
 */
fun MethodSignature.getMethodName(): String {
    return StringBuilder().apply {
        append(declaringTypeName)
        append(".")
        append(method.name)
        append(
            parameterTypes.joinToString(
                prefix = "(",
                separator = ",",
                postfix = ")",
                transform = { it.canonicalName }
            )
        )
    }.toString()
}
