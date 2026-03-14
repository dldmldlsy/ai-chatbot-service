package com.yyy.ai_chatbot_service.feedback.controller

import com.yyy.ai_chatbot_service.feedback.dto.FeedbackListResponse
import com.yyy.ai_chatbot_service.feedback.service.FeedbackQueryService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feedbacks")
class FeedbackQueryController(
    private val feedbackQueryService: FeedbackQueryService
) {

    @GetMapping
    fun getFeedbacks(
        @AuthenticationPrincipal user: User,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "desc") direction: String,
        @RequestParam(required = false) isPositive: Boolean?
    ): FeedbackListResponse {
        return feedbackQueryService.getFeedbacks(user.email, page, size, direction, isPositive)
    }
}
