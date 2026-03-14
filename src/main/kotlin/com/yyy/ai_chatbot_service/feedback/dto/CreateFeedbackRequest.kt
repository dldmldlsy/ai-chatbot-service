package com.yyy.ai_chatbot_service.feedback.dto

import jakarta.validation.constraints.NotNull

data class CreateFeedbackRequest(
    @field:NotNull
    val chatId: Long,

    @field:NotNull
    val isPositive: Boolean
)
