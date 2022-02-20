package com.ioleynikov

import com.beust.klaxon.Klaxon
import com.ioleynikov.testModel.User
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.random.Random

class UsersProviderTest {
    private val random = Random(LocalDateTime.now().second)
    private fun createUser(): User {
        return Klaxon().parse<User>(
            "{\n" +
                    "  \"username\": \"johndoe${random.nextInt()}\",\n" +
                    "  \"firstName\": \"John\",\n" +
                    "  \"lastName\": \"Doe\",\n" +
                    "  \"email\": \"bestjohn@doe.com\",\n" +
                    "  \"phone\": \"+71002003040\"\n" +
                    "}"
        )!!
    }

    private fun createUserRequest(user: User): Response {
        return app(Request(Method.POST, "/user").body(Klaxon().toJsonString(user)))
    }

    private fun getUserRequest(username: String): Response {
        return app(Request(Method.GET, "/user/${username}"))
    }

    private fun updateUserRequest(username: String, user: User): Response {
        return app(Request(Method.PUT, "/user/${username}").body(Klaxon().toJsonString(user)))
    }

    @Test
    fun `Ping test`() {
        assertEquals(app(Request(Method.GET, "/ping")), Response(OK).body("pong"))
    }

    @Test
    fun `Create User Test`() {
        val testUser = createUser()
        val createUserResponseResult = createUserRequest(testUser!!)
        val getUserResponseResult = getUserRequest(testUser!!.username!!)
        val userFromResponse = Klaxon().parse<User>(getUserResponseResult.body.toString())

        assertEquals(createUserResponseResult, Response(OK))
        assertEquals(userFromResponse, testUser)
    }

//    @Test
//    fun `Create User Test Idempotence `() {
//        val testUser = createUser()
//        val createUserResponseResult = createUserRequest(testUser)
//        val createUserResponseSecondResult = createUserRequest(testUser)
//        val getUserResponseResult = getUserRequest(testUser.username!!)
//        val userFromResponse = Klaxon().parse<User>(getUserResponseResult.body.toString())
//
//        assertEquals(createUserResponseResult, Response(OK))
//        assertEquals(createUserResponseSecondResult, Response(OK))
//        assertEquals(userFromResponse, testUser)
//    }


    @Test
    fun `Success Update User Test`() {
        val testUser = createUser()
        val createUserResponseResult = createUserRequest(testUser)
        val newUser = User(testUser)
        newUser.firstName = "newFirstName"
        newUser.lastName = "newLastName"

        val userUpdateResponseResult = updateUserRequest(testUser.username!!, newUser)
        val userFromResponse = getUserRequest(testUser.username!!)

        assertEquals(createUserResponseResult, Response(OK))
        assertEquals(userUpdateResponseResult, Response(OK))
        assertEquals(Klaxon().parse<User>(userFromResponse.body.toString()) , newUser)
    }

    @Test
    fun `Success Delete User Test`() {

    }
}
