package com.start.up.project.auth.security

import com.start.up.project.env.EnvironmentVariables
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JWTAuthorizationFilter(authManager: AuthenticationManager, val env: Environment) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader("${env.getProperty(EnvironmentVariables.AUTH_HEADER.propertyName)}")

        if (header == null || !header.startsWith("${env.getProperty(EnvironmentVariables.TOKEN_PREFIX.propertyName)} ")) {
            chain.doFilter(req, res)
            return
        }

        val authentication = getAuthentication(req)

        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token: String = request.getHeader("${env.getProperty(EnvironmentVariables.AUTH_HEADER.propertyName)}")
        val user: String = UserUtil.getUser(token, env.getProperty(EnvironmentVariables.JWT_SECRET.propertyName), env.getProperty(EnvironmentVariables.TOKEN_PREFIX.propertyName))
        return UsernamePasswordAuthenticationToken(user, null, ArrayList<GrantedAuthority>())
    }
}