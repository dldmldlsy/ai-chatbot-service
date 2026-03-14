package com.yyy.ai_chatbot_service.chat.domain

import com.yyy.ai_chatbot_service.thread.domain.ChatThread
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
@Table(name = "chats")
open class Chat protected constructor() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    open lateinit var thread: ChatThread
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open lateinit var user: User
        protected set

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    open lateinit var question: String
        protected set

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    open lateinit var answer: String
        protected set

    @Column(name = "model", nullable = false)
    open lateinit var model: String
        protected set

    @Column(name = "is_streaming", nullable = false)
    open var isStreaming: Boolean = false
        protected set

    @Column(name = "created_at", nullable = false)
    open lateinit var createdAt: LocalDateTime
        protected set

    constructor(
        thread: ChatThread,
        user: User,
        question: String,
        answer: String,
        model: String,
        isStreaming: Boolean,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) : this() {
        this.thread = thread
        this.user = user
        this.question = question
        this.answer = answer
        this.model = model
        this.isStreaming = isStreaming
        this.createdAt = createdAt
    }

    companion object {
        fun create(
            thread: ChatThread,
            user: User,
            question: String,
            answer: String,
            model: String,
            isStreaming: Boolean
        ): Chat {
            return Chat(
                thread = thread,
                user = user,
                question = question,
                answer = answer,
                model = model,
                isStreaming = isStreaming,
                createdAt = LocalDateTime.now()
            )
        }
    }
}