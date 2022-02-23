package com.ioleynikov

import com.ioleynikov.handlers.routingHttpHandler
import com.ioleynikov.handlers.usersProviderConfig
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(routingHttpHandler)
    val server = printingApp.asServer(SunHttp(usersProviderConfig.server.port)).start()
    println("Server started on " + server.port())
}
