package com.yyy.ai_chatbot_service.chat.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateChatRequest(
    @field:NotBlank
    @field:Size(min = 1, max = 4000)
    val question: String,

    val isStreaming: Boolean = false,

    val model: String? = null
)
