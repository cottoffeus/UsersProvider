package com.ioleynikov.config

data class Database(
    val username: String = "",
    val password: String = "",
    val host: String = "",
    val port: Int = 0,
    val appSchema: String = "",
    val database: String = ""
)
