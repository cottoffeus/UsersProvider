package com.ioleynikov.testModel

data class User(
    var username: String? = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var phone: String = ""
) {
    constructor(user: User) : this(
        username = user.username,
        firstName = user.firstName,
        lastName = user.lastName,
        email = user.email,
        phone = user.phone
    )
}
