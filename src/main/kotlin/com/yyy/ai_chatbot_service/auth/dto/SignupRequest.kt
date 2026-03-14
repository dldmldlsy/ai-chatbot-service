package com.yyy.ai_chatbot_service.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignupRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 100)
    val password: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val name: String
)
