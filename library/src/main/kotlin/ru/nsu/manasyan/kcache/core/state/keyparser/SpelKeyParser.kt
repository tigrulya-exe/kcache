package ru.nsu.manasyan.kcache.core.state.keyparser

import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import ru.nsu.manasyan.kcache.aspect.KCacheableAspect

class SpelKeyParser(
    private val expressionParser: ExpressionParser
) : KeyParser {
    private companion object {
        private const val SPEL_PREFIX = "#"
        private const val SPEL_CONTEXT_ARGS_KEY = "args"
    }

    override fun parse(keyExpression: String, methodArgs: Array<Any>): String {
        if (!keyExpression.startsWith(SPEL_PREFIX)) {
            return keyExpression
        }
        val context: EvaluationContext = StandardEvaluationContext().apply {
            setVariable(SPEL_CONTEXT_ARGS_KEY, methodArgs)
        }
        return expressionParser
            .parseExpression(keyExpression)
            .getValue(context)
            ?.toString()
            ?: throw IllegalArgumentException("Key should not be null")
    }
}