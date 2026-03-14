package com.yyy.ai_chatbot_service.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
open class User protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long? = null
        protected set

    @Column(name = "email", nullable = false, unique = true)
    open lateinit var email: String
        protected set

    @Column(name = "password", nullable = false)
    open lateinit var password: String
        protected set

    @Column(name = "name", nullable = false)
    open lateinit var name: String
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    open lateinit var role: UserRole
        protected set

    @Column(name = "created_at", nullable = false)
    open lateinit var createdAt: LocalDateTime
        protected set

    constructor(
        email: String,
        password: String,
        name: String,
        role: UserRole = UserRole.MEMBER,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) : this() {
        this.email = email
        this.password = password
        this.name = name
        this.role = role
        this.createdAt = createdAt
    }

    companion object {
        fun create(
            email: String,
            password: String,
            name: String,
            role: UserRole = UserRole.MEMBER
        ): User {
            return User(
                email = email,
                password = password,
                name = name,
                role = role,
                createdAt = LocalDateTime.now()
            )
        }
    }
}