package com.ioleynikov

import com.ioleynikov.handlers.routingHttpHandler
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.server.SunHttp
import org.http4k.server.asServer

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(routingHttpHandler)
    val server = printingApp.asServer(SunHttp(9000)).start()
    println("Server started on " + server.port())
}
