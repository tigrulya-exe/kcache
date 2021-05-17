package ru.nsu.manasyan.kcache.core.state.provider

import ru.nsu.manasyan.kcache.util.LoggerProperty
import javax.sql.DataSource

abstract class JdbcStateProvider(
    private val dataSource: DataSource
) : NewStateProvider {
    private val logger by LoggerProperty()

    override fun provide(tableName: String): String {
        return runCatching {
            dataSource
                .connection
                .createStatement()
                .use { statement ->
                    statement.executeQuery(
                        getCheckSumQuery(tableName)
                    ).use { resultSet ->
                        resultSet.next()
                        resultSet.getString(getCheckSumColumnIndex())
                    }
                }
        }.getOrElse { exc ->
            logger.error("Error during building JDBC checksum: ${exc.localizedMessage}")
            throw exc
        }
    }

    protected abstract fun getCheckSumQuery(tableName: String): String

    protected abstract fun getCheckSumColumnIndex(): Int
}