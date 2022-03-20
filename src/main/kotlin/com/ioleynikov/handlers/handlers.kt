package com.ioleynikov.handlers

import com.beust.klaxon.Klaxon
import com.ioleynikov.config.UsersProviderConfig
import com.ioleynikov.model.ResultResponse
import com.ioleynikov.model.User
import com.ioleynikov.model.enums.ResultResponseCodes
import com.ioleynikov.model.enums.ResultResponseMessages
import com.ioleynikov.repositories.UsersRepository
import com.sksamuel.hoplite.ConfigLoader
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes

val usersProviderConfig = ConfigLoader().loadConfigOrThrow<UsersProviderConfig>("/users_provider.properties")
val usersRepository: UsersRepository = UsersRepository(config = usersProviderConfig)
val klaxon: Klaxon = Klaxon()
val routingHttpHandler: HttpHandler = routes(

    "/ping" bind Method.GET to {
        Response(Status.OK).body("pong")
    },

    "/user" bind Method.POST to { request ->
        val isUserCreated = usersRepository.createUser(klaxon.parse<User>(request.bodyString()))
        if (isUserCreated) {
            Response(Status.OK)
        } else {
            Response(Status.BAD_REQUEST).body(
                klaxon.toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.ERROR.code,
                        message = ResultResponseMessages.USER_ALREADY_EXISTS.message
                    )
                )
            )
        }
    },

    "/user/{username}" bind Method.PUT to { request ->
        val isUserUpdated = usersRepository.updateUser(
            request.path("username")!!.toString(),
            klaxon.parse<User>(request.bodyString())
        )
        if (isUserUpdated) {
            Response(Status.OK).body(
                klaxon.toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.SUCCESS.code,
                        message = ResultResponseMessages.USER_UPDATED.message
                    )
                )
            )
        } else {
            Response(Status.NOT_FOUND).body(
                klaxon.toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.ERROR.code,
                        message = ResultResponseMessages.USER_NOT_FOUND.message
                    )
                )
            )
        }
    },

    "/user/{username}" bind Method.DELETE to { request ->
        val isUserDeleted = usersRepository.deleteUser((request.path("username")!!.toString()))
        if (isUserDeleted) {
            Response(Status.OK).body(
                klaxon.toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.SUCCESS.code,
                        message = ResultResponseMessages.USER_DELETED.message
                    )
                )
            )
        } else {
            Response(Status.NO_CONTENT)
        }
    },
    "/user/{username}" bind Method.GET to { request ->
        val user = usersRepository.getUser(request.path("username")!!.toString())
        if (user == null) {
            Response(Status.NOT_FOUND).body(
                klaxon.toJsonString(
                    ResultResponse(
                        code = ResultResponseCodes.SUCCESS.code,
                        message = ResultResponseMessages.USER_NOT_FOUND.message
                    )
                )
            )
        } else {
            Response(Status.OK).body(klaxon.toJsonString(user))
        }
    }

)
