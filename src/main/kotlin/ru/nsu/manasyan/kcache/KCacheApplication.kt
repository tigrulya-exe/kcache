package ru.nsu.manasyan.kcache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KCacheApplication

fun main(args: Array<String>) {
    runApplication<KCacheApplication>(*args)
}