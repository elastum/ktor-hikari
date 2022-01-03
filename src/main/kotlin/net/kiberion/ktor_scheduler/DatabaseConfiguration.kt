package net.kiberion.ktor_scheduler

class DatabaseConfiguration private constructor() {

    companion object {
        fun create(): DatabaseConfiguration {
            return DatabaseConfiguration()
        }
    }
}
