package ru.nsu.manasyan.kcache.core.state.provider

import javax.sql.DataSource

class PostgresMd5NewStateProvider(
    dataSource: DataSource
) : JdbcStateProvider(dataSource) {
    override fun getCheckSumQuery(tableName: String) =
        """ 
            SELECT md5(CAST((array_agg(u.* order by id))AS text))
            FROM $tableName u;
        """

    override fun getCheckSumColumnIndex() = 1
}