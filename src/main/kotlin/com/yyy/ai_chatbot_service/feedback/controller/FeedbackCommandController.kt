package com.yyy.ai_chatbot_service.feedback.controller

import com.yyy.ai_chatbot_service.feedback.dto.CreateFeedbackRequest
import com.yyy.ai_chatbot_service.feedback.dto.FeedbackResponse
import com.yyy.ai_chatbot_service.feedback.service.FeedbackCommandService
import com.yyy.ai_chatbot_service.user.domain.User
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal

@RestController
@RequestMapping("/api/feedbacks")
class FeedbackCommandController(
    private val feedbackCommandService: FeedbackCommandService
) {

    @PostMapping
    fun createFeedback(
        @AuthenticationPrincipal user: User,
        @Valid @RequestBody request: CreateFeedbackRequest
    ): FeedbackResponse {
        return feedbackCommandService.createFeedback(user.email, request)
    }
}
