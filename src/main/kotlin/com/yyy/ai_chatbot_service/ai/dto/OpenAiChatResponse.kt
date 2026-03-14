package com.yyy.ai_chatbot_service.ai.dto

data class OpenAiChatResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: Message?
    )

    data class Message(
        val role: String?,
        val content: String?
    )
}
