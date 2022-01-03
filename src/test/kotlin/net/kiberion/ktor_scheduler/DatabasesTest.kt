package net.kiberion.ktor_scheduler

import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.server.testing.withTestApplication
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.concurrent.TimeUnit

private fun Application.testModule() {
    install(Databases) {
    }
}

class DatabasesTest {

    @Test
    fun `should close connections after application is stopped`(): Unit =
        withTestApplication({
            testModule()
            dataSources {
                database(
                    dataSourceId = "h2",
                    jdbcUrl = "jdbc:h2:mem:test_mem",
                    username = "sa",
                    password = ""
                )
            }
        }) {
            val dataSource: HikariDataSource = this.application.attributes[Databases.DatabasesKey].getDataSource("h2") as HikariDataSource
            expectThat(dataSource.isClosed).isEqualTo(false)
            this.application.dispose()
            await().atMost(90, TimeUnit.SECONDS).until {
                dataSource.isClosed
            }
        }

}
