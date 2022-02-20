package com.ioleynikov

import com.beust.klaxon.Klaxon
import com.ioleynikov.model.User
import com.ioleynikov.repositories.UsersRepository
import mu.KotlinLogging
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val usersRepository: UsersRepository = UsersRepository()
private val logger = KotlinLogging.logger {}

val app: HttpHandler = routes(

    "/ping" bind Method.GET to {
        Response(OK).body("pong")
    },

    "/user" bind Method.POST to { request ->
        usersRepository.createUser(Klaxon().parse<User>(request.bodyString()))
        Response(OK)
    },

    "/user/{username}" bind Method.PUT to { request ->
        usersRepository.updateUser(request.path("username")!!.toString(), Klaxon().parse<User>(request.bodyString()))
        Response(OK)
    },

    "/user/{username}" bind Method.DELETE to { request ->
        usersRepository.deleteUser((request.path("username")!!.toString()))
        Response(OK)
    },
    "/user/{username}" bind Method.GET to { request ->
        val user = usersRepository.getUser(request.path("username")!!.toString())
        val body = Klaxon().toJsonString(user)
        Response(OK).body(body)
    }

)

fun main() {
    logger.error("starting")
    logger.warn("starting warn")
    logger.info("starting info")
    logger.debug("starting debug")
    logger.trace("starting trace")
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())

}
