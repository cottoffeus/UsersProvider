package com.ioleynikov.model.enums

enum class ResultResponseMessages (val message: String) {
    USER_NOT_FOUND("User not found"),
    USER_DELETED("User deleted"),
    USER_UPDATED("User updated"),
    USER_ALREADY_EXISTS("User already exists")
}