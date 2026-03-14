package com.yyy.ai_chatbot_service.feedback.dto

import jakarta.validation.constraints.NotBlank

data class UpdateFeedbackStatusRequest(
    @field:NotBlank
    val status: String
)
