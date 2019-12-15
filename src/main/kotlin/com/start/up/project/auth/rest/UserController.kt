package com.start.up.project.auth.rest

import com.start.up.project.auth.entity.User
import com.start.up.project.auth.repository.UserRepository
import com.start.up.project.auth.security.JWTAuthenticationFilter
import com.start.up.project.auth.security.UserUtil
import com.start.up.project.env.EnvironmentVariables
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userRepository: UserRepository,
                     private val passwordEncoder: PasswordEncoder,
                     private val env: Environment) {
    val logger: Logger = LoggerFactory.getLogger(UserRepository::class.java);
    @PostMapping("/sign-up")
    fun signUp(@RequestBody user: User) {
        logger.info("Try signing up user : {}", user.username)
        user.password = passwordEncoder.encode(user.password)
        userRepository.save(user)
    }

    @PostMapping("/current/user")
    fun token(@RequestHeader auth: String): User {
        val username = UserUtil.getUser(auth, env.getProperty(EnvironmentVariables.JWT_SECRET.propertyName), env.getProperty(EnvironmentVariables.TOKEN_PREFIX.propertyName))
        logger.info("Get information for user :{}", username)
        return userRepository.findByUsername(username)
    }

}