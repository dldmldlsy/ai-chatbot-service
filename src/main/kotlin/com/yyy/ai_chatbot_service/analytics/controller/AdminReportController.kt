package com.yyy.ai_chatbot_service.analytics.controller

import com.yyy.ai_chatbot_service.analytics.service.AdminReportService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/reports")
class AdminReportController(
    private val adminReportService: AdminReportService
) {

    @GetMapping("/chats")
    fun downloadChatReport(
        @AuthenticationPrincipal user: User
    ): ResponseEntity<ByteArray> {
        return adminReportService.generateChatReport(user.email)
    }
}
