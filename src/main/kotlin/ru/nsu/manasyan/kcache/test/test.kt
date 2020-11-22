package ru.nsu.manasyan.kcache.test

import org.springframework.core.annotation.AnnotationUtils
import org.springframework.util.ReflectionUtils
import kotlin.reflect.KClass

annotation class BaseAnnotation(
    val property: String
)

@BaseAnnotation(property = "")
annotation class ChildAnnotation(
    val property: KClass<*>
)

@ChildAnnotation(TestClass::class)
class TestClass {}

fun main1() {
    val testObj = TestClass()

    val testClass = testObj::class
    val annotations = testClass.annotations
    println(annotations)

    val childAnnotation = AnnotationUtils.findAnnotation(
        TestClass::class.java,
        ChildAnnotation::class.java
    )
    println(childAnnotation)

    val baseAnnotation = AnnotationUtils.findAnnotation(
        ChildAnnotation::class.java,
        BaseAnnotation::class.java
    )
    println(baseAnnotation)

    
    KClass::class.annotations
    testClass.annotations
}