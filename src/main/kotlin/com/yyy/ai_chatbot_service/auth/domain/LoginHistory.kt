package com.yyy.ai_chatbot_service.auth.domain

import com.yyy.ai_chatbot_service.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "login_histories")
open class LoginHistory protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var user: User
        protected set

    @Column(name = "created_at", nullable = false)
    open lateinit var createdAt: LocalDateTime
        protected set

    constructor(
        user: User,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) : this() {
        this.user = user
        this.createdAt = createdAt
    }

    companion object {
        fun create(user: User): LoginHistory {
            return LoginHistory(
                user = user,
                createdAt = LocalDateTime.now()
            )
        }
    }
}