package com.yyy.ai_chatbot_service.thread.domain

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
@Table(name = "chat_threads")
open class ChatThread protected constructor() {

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

    @Column(name = "last_question_at", nullable = false)
    open lateinit var lastQuestionAt: LocalDateTime
        protected set

    @Column(name = "deleted_at")
    open var deletedAt: LocalDateTime? = null
        protected set

    constructor(
        user: User,
        createdAt: LocalDateTime = LocalDateTime.now(),
        lastQuestionAt: LocalDateTime = LocalDateTime.now()
    ) : this() {
        this.user = user
        this.createdAt = createdAt
        this.lastQuestionAt = lastQuestionAt
    }

    fun updateLastQuestionAt(time: LocalDateTime) {
        this.lastQuestionAt = time
    }

    fun delete(time: LocalDateTime) {
        this.deletedAt = time
    }

    fun isDeleted(): Boolean {
        return this.deletedAt != null
    }

    companion object {
        fun create(user: User): ChatThread {
            val now = LocalDateTime.now()
            return ChatThread(
                user = user,
                createdAt = now,
                lastQuestionAt = now
            )
        }
    }
}