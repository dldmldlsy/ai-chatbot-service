package com.yyy.ai_chatbot_service.feedback.dto

data class FeedbackListResponse(
    val content: List<FeedbackResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val hasNext: Boolean
)
