package net.kiberion.ktor_scheduler

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.util.AttributeKey
import java.io.Closeable
import java.lang.IllegalArgumentException
import javax.sql.DataSource
import kotlin.collections.HashMap

class Databases(
    val configuration: DatabaseConfiguration,
    val dataSources: MutableMap<String, HikariDataSource> = HashMap()
): Closeable {

    /**
     * Connect to DB and register datasource instance
     */
    fun createHikariDataSource(
        dataSourceId: String,
        jdbcUrl: String,
        username: String,
        password: String,
    ) {
        val hikariConfig = HikariConfig()
        hikariConfig.jdbcUrl = jdbcUrl
        hikariConfig.username = username
        hikariConfig.password = password
        val dataSource = HikariDataSource(hikariConfig)
        dataSources[dataSourceId] = dataSource
    }

    fun getDataSource(
        dataSourceId: String
    ): DataSource {
        return dataSources[dataSourceId]
            ?: throw IllegalArgumentException("DataSource $dataSourceId is not registered")
    }

    companion object Feature : ApplicationFeature<Application, DatabaseConfiguration, Databases> {
        val DatabasesKey = AttributeKey<Databases>("Database")
        override val key: AttributeKey<Databases>
            get() = DatabasesKey

        override fun install(
            pipeline: Application,
            configure: DatabaseConfiguration.() -> Unit
        ): Databases {
            val configuration = DatabaseConfiguration.create()
            configuration.apply(configure)

            val databases = Databases(configuration)
            pipeline.attributes.put(key, databases)

            return databases
        }
    }

    override fun close() {
        dataSources.forEach {
            it.value.close()
        }
    }
}
