package com.start.up.project.env

enum class EnvironmentVariables(val propertyName: String) {
    JWT_SECRET("jwt.secret"),
    TOKEN_PREFIX("token.prefix"),
    AUTH_HEADER("auth.header"),
    TOKEN_EXPIRE_TIME("token.expire.time")
}