package it.marcointroini

import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val host = environment.config.property( "ktor.deployment.host").getString()
    val port = environment.config.property( "ktor.deployment.port").getString()

    println("Running on $host:$port")
}
