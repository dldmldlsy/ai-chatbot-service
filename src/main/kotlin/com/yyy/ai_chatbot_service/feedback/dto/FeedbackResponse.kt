package com.yyy.ai_chatbot_service.feedback.dto

import com.yyy.ai_chatbot_service.feedback.domain.FeedbackStatus
import java.time.LocalDateTime

data class FeedbackResponse(
    val id: Long,
    val userId: Long,
    val chatId: Long,
    val isPositive: Boolean,
    val status: FeedbackStatus,
    val createdAt: LocalDateTime
)
