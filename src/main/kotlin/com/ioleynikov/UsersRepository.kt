package com.ioleynikov

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
        )), ConnectionPoolConfiguration()
    )

    fun createUser(user: User?) {
        connection.connect().get()
        val future = connection.sendPreparedStatement("INSERT INTO ${appSchema}.users (username) VALUES('${user?.username}')")
        val queryResult = future.get()
        println(queryResult.statusMessage)
        println(queryResult.toString())
        connection.disconnect().get()
    }
}