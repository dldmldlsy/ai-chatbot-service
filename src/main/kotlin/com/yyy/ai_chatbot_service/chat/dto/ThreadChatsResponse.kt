package com.yyy.ai_chatbot_service.chat.dto

import java.time.LocalDateTime

data class ThreadChatsResponse(
    val threadId: Long,
    val userId: Long,
    val createdAt: LocalDateTime,
    val lastQuestionAt: LocalDateTime,
    val chats: List<ChatSummaryResponse>
)
