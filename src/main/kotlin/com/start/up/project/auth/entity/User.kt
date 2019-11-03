package com.start.up.project.auth.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*


@Entity
class User() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0
    @Column(unique = true)
    var username: String = ""
    @JsonIgnore
    var password: String = ""
    @Version
    val version: Long? = null
}