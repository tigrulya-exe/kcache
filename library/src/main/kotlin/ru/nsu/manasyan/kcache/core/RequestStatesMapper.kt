package ru.nsu.manasyan.kcache.core

interface RequestStatesMapper {
    fun getStates(requestName: String): Array<String>?

    fun setStates(requestName: String, states: Array<String>)
}