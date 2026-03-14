package com.yyy.ai_chatbot_service.ai.dto

data class OpenAiChatRequest(
    val model: String,
    val messages: List<OpenAiMessage>
)
