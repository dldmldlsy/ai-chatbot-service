package com.yyy.ai_chatbot_service.analytics.controller

import com.yyy.ai_chatbot_service.analytics.dto.ActivitySummaryResponse
import com.yyy.ai_chatbot_service.analytics.service.AdminAnalyticsService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/analytics")
class AdminAnalyticsController(
    private val adminAnalyticsService: AdminAnalyticsService
) {

    @GetMapping("/activity")
    fun getActivity(
        @AuthenticationPrincipal user: User
    ): ActivitySummaryResponse {
        return adminAnalyticsService.getActivitySummary(user.email)
    }
}
