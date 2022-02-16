package com.ioleynikov.repositories

import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.ConnectionPoolConfiguration
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import com.ioleynikov.model.User

class UsersRepository {
    private val appSchema : String = "users_provider"
    private val connection: Connection = ConnectionPool(
        PostgreSQLConnectionFactory(
            Configuration(
                username = "app",
                password = "pass",
                host = "localhost",
                port = 5432,
                database = "db"
            )
        ), ConnectionPoolConfiguration()
    )

    fun createUser( user: User?) {

        val future = connection.sendPreparedStatement("INSERT INTO ${appSchema}.users " +
                "(username, first_name, last_name, email, phone) " +
                "VALUES('${user?.username}', '${user?.firstName}', '${user?.lastName}', '${user?.email}', '${user?.phone}')")
        val queryResult = future.get()
        println(queryResult.statusMessage)
        println(queryResult.toString())

    }

    fun updateUser(username : String, user: User?) {
        val future = connection.sendPreparedStatement("UPDATE ${appSchema}.users SET first_name='${user?.firstName}', last_name='${user?.lastName}', email='${user?.email}', phone='${user?.phone}' where username='${username}'")
        val queryResult = future.get()
        println(queryResult.statusMessage)
        println(queryResult.toString())
    }

    fun deleteUser(user: User?) {
        connection.connect().get()
        val future = connection.sendPreparedStatement("")
        val queryResult = future.get()
        println(queryResult.statusMessage)
        println(queryResult.toString())
        connection.disconnect().get()
    }
}