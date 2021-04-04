package ru.nsu.manasyan.kcache.config

import io.github.classgraph.ClassGraph
import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder

/**
 * BeanPostProcessor, which injects initial states of all table states from [RequestHandlerMetadataContainer] to [StateHolder].
 */
class EntitiesToListenBeanPostProcessor() : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (beanName != "entitiesToListenContainer") {
            return bean
        }
        ClassGraph()
            .enableAnnotationInfo()
            .enableMethodInfo()
            .scan()
            .getClassesWithMethodAnnotation(KCacheableJpa::class.qualifiedName)
            .forEach {
                it.methodInfo.forEach { method ->
                    println(
                    method.getAnnotationInfo(KCacheableJpa::class.qualifiedName)
                        ?.parameterValues
                        ?.get("entities")
                        ?.value?.javaClass
                    )
                }
            }
        return bean
    }
}