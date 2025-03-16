package it.marcointroini

import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import kotlin.time.Duration.Companion.seconds

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val host = environment.config.getProperty("ktor.deployment.host")
    val port = environment.config.getProperty("ktor.deployment.port").toInt()

    println("Running on $host:$port")

    val dataSource = getDataSource(environment.config)
    dataSource.connection
}

private fun ApplicationConfig.getProperty(path: String): String = property(path).getString()

private fun getDataSource(config: ApplicationConfig): HikariDataSource {
    val dbUrl = config.getProperty("database.url")
    val dbUser = config.getProperty("database.user")
    val dbPassword = config.getProperty("database.password")

    return HikariDataSource().apply {
        jdbcUrl = dbUrl
        username = dbUser
        password = dbPassword
        driverClassName = "org.postgresql.Driver"
        maximumPoolSize = 3
        minimumIdle = 2
        idleTimeout = 10.seconds.inWholeMilliseconds
        connectionTimeout = 30.seconds.inWholeMilliseconds
        maxLifetime = 30.seconds.inWholeMilliseconds
    }
}
