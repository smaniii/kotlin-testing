package com.start.up.project.auth.repository

import com.start.up.project.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User
}