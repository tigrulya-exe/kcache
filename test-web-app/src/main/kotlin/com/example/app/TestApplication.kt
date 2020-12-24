package com.example.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestApplication

fun main(args: Array<String>) {
    val context = runApplication<TestApplication>(*args)
    context.beanDefinitionNames.forEach { println(it) }
}