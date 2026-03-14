package com.yyy.ai_chatbot_service.analytics.dto

import java.time.LocalDateTime

data class ActivitySummaryResponse(
    val signupCount: Long,
    val loginCount: Long,
    val chatCount: Long,
    val from: LocalDateTime,
    val to: LocalDateTime
)
