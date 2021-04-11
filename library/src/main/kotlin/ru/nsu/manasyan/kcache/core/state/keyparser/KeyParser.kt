package ru.nsu.manasyan.kcache.core.state.keyparser

interface KeyParser {
    fun parse(keyExpression: String, methodArgs: Array<Any>): String
}