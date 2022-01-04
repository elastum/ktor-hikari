# ktor-hikari

ktor plugin for Hikari connection pool.

This plugin is used for managing database connections within an application scope. While generally you would use a DI
injection engine, such as Koin for this, sometimes this is not feasible because your DI plugin depends on other plugins,
which depend on the database, so the database needs to be instantiated before your DI context is.

All connections opened by the plugin are automatically closed when application is stopped.

## Installing

Gradle:

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.kibertoad:ktor-hikari:1.0.0")
}
```

## Example usage

```kotlin
// create DB datasource
fun Application.configureDatabase() {
    install(Databases) {
    }

    dataSources {
        database(
            dataSourceId = "default",
            jdbcUrl = "jdbc:h2:mem:test_mem",
            username = "sa",
            password = ""
        )
    }
}

// accessing datasources from another plugin
fun Application.configureScheduling() {
    val dataSource = this.attributes[Databases.DatabasesKey].getDataSource("default")

    install(Scheduler) {
        storageProvider = H2StorageProvider(dataSource)
    }

    schedule {
        recurringJob("userSync", "*/2 * * * *") {
            // some recurring logic
        }
    }
}
```
