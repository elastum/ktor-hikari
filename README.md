# ktor-hikari
ktor plugin for Hikari connection pool

## Installing


## Example usage

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

```kotlin
fun Application.configureDatabase() {
    install(Databases) {
    }

    dataSources {
        database(
            dataSourceId = "h2",
            jdbcUrl = "jdbc:h2:mem:test_mem",
            username = "sa",
            password = ""
        )
    }
}
```
