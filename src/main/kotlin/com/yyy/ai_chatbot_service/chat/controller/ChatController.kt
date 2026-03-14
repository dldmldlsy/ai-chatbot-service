package com.yyy.ai_chatbot_service.chat.controller

import com.yyy.ai_chatbot_service.chat.dto.CreateChatRequest
import com.yyy.ai_chatbot_service.chat.dto.CreateChatResponse
import com.yyy.ai_chatbot_service.chat.service.ChatService
import com.yyy.ai_chatbot_service.user.domain.User
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping
    fun createChat(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: CreateChatRequest
    ): CreateChatResponse {
        return chatService.createChat(user, request)
    }
}
