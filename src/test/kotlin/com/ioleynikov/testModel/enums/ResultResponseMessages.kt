package com.ioleynikov.testModel.enums

enum class ResultResponseMessages (val message: String) {
    USER_NOT_FOUND("User not found"),
    USER_DELETED("User deleted"),
    USER_ALREADY_EXISTS("User already exists")
}