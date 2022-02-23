package com.ioleynikov.config


data class UsersProviderConfig(
    val database: Database = Database(),
    val server: Server = Server()
)