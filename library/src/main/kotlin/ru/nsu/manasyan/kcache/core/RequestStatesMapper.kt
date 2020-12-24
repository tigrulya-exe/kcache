package ru.nsu.manasyan.kcache.core

interface RequestStatesMapper {
    companion object {
        const val MAPPINGS_FILE_PATH = "requestStatesMapping.json"
    }

    fun getRequestStates(requestName: String): List<String>?

    fun setRequestStates(requestName: String, states: List<String>)

    fun getAllStates(): Map<String, List<String>>
}