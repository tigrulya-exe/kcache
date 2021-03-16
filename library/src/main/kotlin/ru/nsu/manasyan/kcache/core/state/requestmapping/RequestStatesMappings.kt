package ru.nsu.manasyan.kcache.core.state.requestmapping

/**
 * Storage of HTTP-request handler methods' names and
 * names of tables, on which the return value such method depends, mappings
 */
// TODO: rename state -> entity/table
interface RequestStatesMappings {
    companion object {
        const val MAPPINGS_FILE_PATH = "requestStatesMapping.json"
    }

    fun getRequestStates(requestName: String): List<String>?

    fun setRequestStates(requestName: String, states: List<String>)

    fun getAllStates(): Map<String, List<String>>
}