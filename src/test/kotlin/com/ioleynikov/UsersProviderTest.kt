package com.ioleynikov

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.kotest.shouldHaveBody
import org.http4k.kotest.shouldHaveStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UsersProviderTest {

    fun createUser(): Response {
        return app(Request(POST, "/user").body("{\n" +
                "  \"username\": \"johndoe589\",\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"email\": \"bestjohn@doe.com\",\n" +
                "  \"phone\": \"+71002003040\"\n" +
                "}"))
    }
    @Test
    fun `Ping test`() {
        assertEquals(app(Request(GET, "/ping")), Response(OK).body("pong"))
    }

    @Test
    fun `Create User Test`() {
        val userCreateResponse = createUser()
//        val result = Klaxon().parse<User>("{\n" +
//                "  \"username\": \"johndoe589\",\n" +
//                "  \"firstName\": \"John\",\n" +
//                "  \"lastName\": \"Doe\",\n" +
//                "  \"email\": \"bestjohn@doe.com\",\n" +
//                "  \"phone\": \"+71002003040\"\n" +
//                "}")
        assertEquals(userCreateResponse.status, Response(OK))
    }

    @Test
    fun `Success Update User Test`() {
        val userCreateResponse =

        Response(OK).body("{\"userId\": \"\"}")
//        assertEquals(userCreateResponse, )
    }
    @Test

    fun `Success Get User Test`() {
        val userCreateResponse = app(Request(POST, "/user").body("{\n" +
                "  \"username\": \"johndoe589\",\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"email\": \"bestjohn@doe.com\",\n" +
                "  \"phone\": \"+71002003040\"\n" +
                "}"))

        Response(OK).body("{\"userId\": \"\"}")
//        assertEquals(userCreateResponse, )
    }

    fun `Success Delete User Test`() {
        val userCreateResponse = app(Request(POST, "/user").body("{\n" +
                "  \"username\": \"johndoe589\",\n" +
                "  \"firstName\": \"John\",\n" +
                "  \"lastName\": \"Doe\",\n" +
                "  \"email\": \"bestjohn@doe.com\",\n" +
                "  \"phone\": \"+71002003040\"\n" +
                "}"))

        Response(OK).body("{\"userId\": \"\"}")
//        assertEquals(userCreateResponse, )
    }


    @Test
    fun `Check Kotest matcher for http4k work as expected`() {
        val request = Request(GET, "/testing/kotest?a=b").body("http4k is cool").header("my header", "a value")
    
        val response = app(request)
    
        // response assertions
        response shouldHaveStatus OK
        response shouldHaveBody "Echo 'http4k is cool'"
    }

}
