package ru.nsu.manasyan.kcache

import com.github.matfax.klassindex.KlassIndex
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.nsu.manasyan.kcache.core.KCacheable

@SpringBootApplication
class KCacheApplication

fun mainq(args: Array<String>) {
    runApplication<KCacheApplication>(*args)
}

fun main() {
    val classes = KlassIndex.getAnnotated(KCacheable::class)
    classes.forEach { print(it) }
}

class Test {
    @KCacheable()
    fun test() {

    }
}