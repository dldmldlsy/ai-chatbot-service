package com.yyy.ai_chatbot_service.thread.controller

import com.yyy.ai_chatbot_service.thread.service.ChatThreadCommandService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/threads")
class ChatThreadController(
    private val chatThreadCommandService: ChatThreadCommandService
) {

    @DeleteMapping("/{threadId}")
    fun deleteThread(
        @AuthenticationPrincipal user: User,
        @PathVariable threadId: Long
    ): ResponseEntity<Unit> {
        chatThreadCommandService.deleteThread(user.email, threadId)
        return ResponseEntity.noContent().build()
    }
}
