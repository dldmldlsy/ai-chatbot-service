package com.yyy.ai_chatbot_service.auth.dto

import com.yyy.ai_chatbot_service.user.domain.UserRole

data class SignupResponse(
    val id: Long?,
    val email: String,
    val name: String,
    val role: UserRole
)
