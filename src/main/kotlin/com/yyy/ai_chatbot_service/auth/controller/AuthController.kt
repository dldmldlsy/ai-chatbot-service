package com.yyy.ai_chatbot_service.auth.controller

import com.yyy.ai_chatbot_service.auth.dto.LoginRequest
import com.yyy.ai_chatbot_service.auth.dto.LoginResponse
import com.yyy.ai_chatbot_service.auth.dto.SignupRequest
import com.yyy.ai_chatbot_service.auth.dto.SignupResponse
import com.yyy.ai_chatbot_service.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): SignupResponse {
        return authService.signup(request)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        return authService.login(request)
    }
}
