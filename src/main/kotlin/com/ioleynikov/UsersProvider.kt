package com.ioleynikov

import com.beust.klaxon.Klaxon
import com.ioleynikov.config.UsersProviderConfig
import com.ioleynikov.model.ResultResponse
import com.ioleynikov.model.User
import com.ioleynikov.model.enums.ResultResponseCodes
import com.ioleynikov.model.enums.ResultResponseMessages
import com.ioleynikov.repositories.UsersRepository
import com.sksamuel.hoplite.ConfigLoader
import mu.KotlinLogging
import org.http4k.core.*
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val usersProviderConfig = ConfigLoader().loadConfigOrThrow<UsersProviderConfig>("/users_db-dev.yaml")
val usersRepository: UsersRepository = UsersRepository(config = usersProviderConfig)

private val logger = KotlinLogging.logger {}

val app: HttpHandler = routes(

    "/ping" bind Method.GET to {
        Response(OK).body("pong")
    },

    "/user" bind Method.POST to { request ->
        val isUserCreated = usersRepository.createUser(Klaxon().parse<User>(request.bodyString()))
        if (isUserCreated) {
            Response(OK)
        } else {
            Response(Status.BAD_REQUEST).body(
                Klaxon().toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.ERROR.code,
                        message = ResultResponseMessages.USER_ALREADY_EXISTS.message
                    )
                )
            )
        }
    },

    "/user/{username}" bind Method.PUT to { request ->
        usersRepository.updateUser(request.path("username")!!.toString(), Klaxon().parse<User>(request.bodyString()))
        Response(OK)
    },

    "/user/{username}" bind Method.DELETE to { request ->
        usersRepository.deleteUser((request.path("username")!!.toString()))
        //TODO("Сделать ответ зависимым от результата")
        Response(NO_CONTENT).body(
            Klaxon().toJsonString(
                ResultResponse(
                    code = ResultResponseCodes.SUCCESS.code,
                    message = ResultResponseMessages.USER_DELETED.message
                )
            )
        )
    },
    "/user/{username}" bind Method.GET to { request ->
        val user = usersRepository.getUser(request.path("username")!!.toString())
        if (user == null) {
            Response(NOT_FOUND).body(
                Klaxon().toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.SUCCESS.code,
                        message = ResultResponseMessages.USER_NOT_FOUND.message
                    )
                )
            )
        } else {
            val body = Klaxon().toJsonString(user)
            Response(OK).body(body)
        }

    }

)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())

}
