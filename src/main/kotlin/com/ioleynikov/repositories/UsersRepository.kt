package com.ioleynikov.repositories

import com.github.jasync.sql.db.Configuration
import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.ConnectionPoolConfiguration
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.general.ArrayRowData
import com.github.jasync.sql.db.pool.ConnectionPool
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory
import com.ioleynikov.model.User

class UsersRepository {
    private val appSchema: String = "users_provider"
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

    fun createUser(user: User?) : Boolean {

        val future = connection.sendPreparedStatement(
            "INSERT INTO ${appSchema}.users " +
                        "(username, first_name, last_name, email, phone) " +
                    "VALUES('" +
                        "${user?.username}', " +
                        "'${user?.firstName}', " +
                        "'${user?.lastName}', " +
                        "'${user?.email}', " +
                        "'${user?.phone}') " +
                    "ON CONFLICT DO NOTHING"
        )
        val queryResult = future.get()
        return queryResult.rowsAffected.compareTo(0) != 0
    }

    fun updateUser(username: String, user: User?) {
        val future =
            connection.sendPreparedStatement("UPDATE ${appSchema}.users SET first_name='${user?.firstName}', last_name='${user?.lastName}', email='${user?.email}', phone='${user?.phone}' where username='${username}'")
    }

    fun deleteUser(username: String) {
        val future = connection.sendPreparedStatement("DELETE FROM ${appSchema}.users WHERE username='${username}'")
    }

    fun getUser(username: String): User? {
        val future = connection.sendPreparedStatement("SELECT * FROM ${appSchema}.users WHERE username='${username}'")
        val queryResult = future.get()
        val users = parseUsers(queryResult)
        return if (users.isEmpty()) {
            null
        } else {
            users[0]
        }


    }

    private fun parseUsers(queryResult: QueryResult): MutableList<User> {
        val users: MutableList<User> = mutableListOf()
        queryResult.rows.toList().forEach() {
            val userResult = it as ArrayRowData
            users.add(
                User(
                    username = userResult["username"].toString(),
                    firstName = userResult["first_name"].toString(),
                    lastName = userResult["last_name"].toString(),
                    email = userResult["email"].toString(),
                    phone = userResult["phone"].toString()
                )
            )
        }
        return users
    }


}