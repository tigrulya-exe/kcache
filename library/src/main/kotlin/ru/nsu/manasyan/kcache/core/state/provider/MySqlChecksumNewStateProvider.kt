package ru.nsu.manasyan.kcache.core.state.provider

import javax.sql.DataSource

class MySqlChecksumNewStateProvider(
    dataSource: DataSource
) : JdbcStateProvider(dataSource) {
    override fun getCheckSumQuery(tableName: String): String = "CHECKSUM TABLE $tableName"

    override fun getCheckSumColumnIndex() = 2
}