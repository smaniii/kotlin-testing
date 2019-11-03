package com.start.up.project.auth.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.fasterxml.jackson.databind.ObjectMapper
import com.start.up.project.env.EnvironmentVariables
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JWTAuthenticationFilter(
        authManager: AuthenticationManager,
        val env: Environment
) : UsernamePasswordAuthenticationFilter() {

    init {
        setAuthenticationManager(authManager)
    }

    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        val creds = ObjectMapper()
                .readValue(req.inputStream, com.start.up.project.auth.entity.User::class.java)
        return authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        creds.username,
                        creds.password,
                        ArrayList<GrantedAuthority>())
        )
    }

    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {

        val token = JWT.create()
                .withSubject((auth.principal as User).username)
                .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .sign(HMAC512(env.getProperty(EnvironmentVariables.JWT_SECRET.propertyName)))
        res.addHeader("${env.getProperty(EnvironmentVariables.AUTH_HEADER.propertyName)}",
                "${env.getProperty(EnvironmentVariables.TOKEN_PREFIX.propertyName)} $token")
    }
}