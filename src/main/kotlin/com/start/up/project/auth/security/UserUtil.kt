package com.start.up.project.auth.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

object UserUtil {
    fun getUser(token: String, secret: String?, prefix: String?): String {
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token.replace("$prefix ", ""))
                .getSubject()
    }
}