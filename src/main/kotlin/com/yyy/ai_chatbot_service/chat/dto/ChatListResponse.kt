package com.yyy.ai_chatbot_service.chat.dto

data class ChatListResponse(
    val content: List<ThreadChatsResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean
)
