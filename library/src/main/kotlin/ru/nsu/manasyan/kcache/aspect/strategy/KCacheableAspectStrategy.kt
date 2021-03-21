package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.resultbuilder.ResultBuilderFactory

interface KCacheableAspectStrategy {
    var methodSignature: MethodSignature?

    fun getTableStates(): List<String>
    fun getResultBuilderFactory(): ResultBuilderFactory

    fun <R> runIfInitialized(action: () -> R): R = methodSignature?.let {
        action()
    } ?: throw IllegalStateException("First initiate method signature")
}