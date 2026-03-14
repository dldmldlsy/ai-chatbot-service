package com.yyy.ai_chatbot_service.chat.controller

import com.yyy.ai_chatbot_service.chat.dto.ChatListResponse
import com.yyy.ai_chatbot_service.chat.service.ChatQueryService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chats")
class ChatQueryController(
    private val chatQueryService: ChatQueryService
) {

    @GetMapping
    fun getChats(
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "desc") direction: String
    ): ChatListResponse {
        return chatQueryService.getChats(user.email, page, size, direction)
    }
}
