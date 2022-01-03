package net.kiberion.ktor_scheduler

import io.ktor.application.Application
import io.ktor.application.feature
import io.ktor.util.pipeline.ContextDsl

@ContextDsl
fun Application.dataSources(configuration: Databases.() -> Unit): Databases =
    feature(Databases).apply(configuration)

@ContextDsl
fun Databases.database(
    dataSourceId: String,
    jdbcUrl: String,
    username: String,
    password: String,
) {
    createHikariDataSource(
        dataSourceId, jdbcUrl, username, password
    )
}
