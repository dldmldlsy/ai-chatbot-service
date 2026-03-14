package com.yyy.ai_chatbot_service.chat.dto

import java.time.LocalDateTime

data class CreateChatResponse(
    val threadId: Long?,
    val chatId: Long?,
    val question: String,
    val answer: String,
    val model: String,
    val isStreaming: Boolean,
    val createdAt: LocalDateTime
)
