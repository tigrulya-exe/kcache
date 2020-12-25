package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.core.UpdateState
import ru.nsu.manasyan.kcache.util.LoggerProperty
import java.time.Instant
import java.time.format.DateTimeFormatter

@Aspect
class UpdateStateAspect(
    private val stateHolder: StateHolder,
) {
    private val logger by LoggerProperty()

    @After("@annotation(ru.nsu.manasyan.kcache.core.UpdateState)")
    fun wrapUpdateStateMethod(joinPoint: JoinPoint) {
        val method = (joinPoint.signature as MethodSignature).method
        val updatedTables = getAnnotationInstance<UpdateState>(method).tables

        updatedTables.forEach {
            val newState = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            stateHolder.setState(it, newState)
            logger.debug("Table $it was updated: $newState")
        }
    }
}