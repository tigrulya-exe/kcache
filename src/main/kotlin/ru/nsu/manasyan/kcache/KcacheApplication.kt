package ru.nsu.manasyan.kcache

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KcacheApplication

fun main(args: Array<String>) {
	runApplication<KcacheApplication>(*args)
}
