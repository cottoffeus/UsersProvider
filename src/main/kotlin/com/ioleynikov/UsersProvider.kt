package com.ioleynikov

import com.beust.klaxon.Klaxon
import com.ioleynikov.model.User
import com.ioleynikov.repositories.UsersRepository
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Method.GET
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

val app: HttpHandler = routes(

    "/ping" bind GET to {
        Response(OK).body("pong")
    },

    "/testing/kotest" bind GET to {request ->
        Response(OK).body("Echo '${request.bodyString()}'")
    },

    "/user" bind Method.POST to { request ->
        usersRepository.createUser(Klaxon().parse<User>(request.bodyString()))
        Response(OK)
    },

    "/user/{username}" bind Method.PUT to { request ->
        usersRepository.updateUser(request.path("username")!!.toString(), Klaxon().parse<User>(request.bodyString()))
        Response(OK)
    }

//    "/contract/api/v1" bind contract {
//        renderer = OpenApi3(ApiInfo("UsersProvider API", "v1.0"), Jackson)
//
//        // Return Swagger API definition under /contract/api/v1/swagger.json
//        descriptionPath = "/swagger.json"
//
//        // You can use security filter tio protect routes
//        security = ApiKeySecurity(Query.int().required("api"), { it == 42 }) // Allow only requests with &api=42
//
//        // Add contract routes
//        routes += ExampleContractRoute()
//    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())

}
