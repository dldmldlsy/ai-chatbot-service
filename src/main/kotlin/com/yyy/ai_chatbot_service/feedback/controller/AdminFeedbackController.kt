package com.yyy.ai_chatbot_service.feedback.controller

import com.yyy.ai_chatbot_service.feedback.dto.FeedbackResponse
import com.yyy.ai_chatbot_service.feedback.dto.UpdateFeedbackStatusRequest
import com.yyy.ai_chatbot_service.feedback.service.FeedbackCommandService
import com.yyy.ai_chatbot_service.user.domain.User
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/feedbacks")
class AdminFeedbackController(
    private val feedbackCommandService: FeedbackCommandService
) {

    @PatchMapping("/{feedbackId}/status")
    fun updateStatus(
        @AuthenticationPrincipal user: User,
        @PathVariable feedbackId: Long,
        @Valid @RequestBody request: UpdateFeedbackStatusRequest
    ): FeedbackResponse {
        return feedbackCommandService.updateStatus(user.email, feedbackId, request)
    }
}
