package com.ioleynikov

import com.ioleynikov.routes.ExampleContractRoute
import org.http4k.contract.bind
import org.http4k.contract.contract
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.contract.security.ApiKeySecurity
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters.PrintRequest
import org.http4k.format.Jackson
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer

val app: HttpHandler = routes(
    "/ping" bind GET to {
        Response(OK).body("pong")
    },

    "/testing/kotest" bind GET to {request ->
        Response(OK).body("Echo '${request.bodyString()}'")
    },

    "/contract/api/v1" bind contract {
        renderer = OpenApi3(ApiInfo("UsersProvider API", "v1.0"), Jackson)
    
        // Return Swagger API definition under /contract/api/v1/swagger.json
        descriptionPath = "/swagger.json"
    
        // You can use security filter tio protect routes
        security = ApiKeySecurity(Query.int().required("api"), { it == 42 }) // Allow only requests with &api=42
    
        // Add contract routes
        routes += ExampleContractRoute()
    }
)

fun main() {
    val printingApp: HttpHandler = PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(9000)).start()

    println("Server started on " + server.port())
}
