package com.yyy.ai_chatbot_service.feedback.domain

import com.yyy.ai_chatbot_service.chat.domain.Chat
import com.yyy.ai_chatbot_service.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "feedbacks",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_feedback_user_chat",
            columnNames = ["user_id", "chat_id"]
        )
    ]
)
open class Feedback protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var user: User
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    open lateinit var chat: Chat
        protected set

    @Column(name = "is_positive", nullable = false)
    open var isPositive: Boolean = false
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    open lateinit var status: FeedbackStatus
        protected set

    @Column(name = "created_at", nullable = false)
    open lateinit var createdAt: LocalDateTime
        protected set

    constructor(
        user: User,
        chat: Chat,
        isPositive: Boolean,
        status: FeedbackStatus = FeedbackStatus.PENDING,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) : this() {
        this.user = user
        this.chat = chat
        this.isPositive = isPositive
        this.status = status
        this.createdAt = createdAt
    }

    fun resolve() {
        this.status = FeedbackStatus.RESOLVED
    }

    fun changeStatus(status: FeedbackStatus) {
        this.status = status
    }

    companion object {
        fun create(
            user: User,
            chat: Chat,
            isPositive: Boolean
        ): Feedback {
            return Feedback(
                user = user,
                chat = chat,
                isPositive = isPositive,
                status = FeedbackStatus.PENDING,
                createdAt = LocalDateTime.now()
            )
        }
    }
}