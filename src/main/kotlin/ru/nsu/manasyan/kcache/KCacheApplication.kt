package ru.nsu.manasyan.kcache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import ru.nsu.manasyan.kcache.api.StateHolder

@SpringBootApplication
class KCacheApplication

fun main(args: Array<String>) {
	runApplication<KCacheApplication>(*args)
}