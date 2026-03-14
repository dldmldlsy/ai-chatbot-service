package com.yyy.ai_chatbot_service.analytics.service

import com.yyy.ai_chatbot_service.analytics.dto.ActivitySummaryResponse
import com.yyy.ai_chatbot_service.auth.repository.LoginHistoryRepository
import com.yyy.ai_chatbot_service.chat.repository.ChatRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AdminAnalyticsService(
    private val userRepository: UserRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val chatRepository: ChatRepository
) {

    @Transactional(readOnly = true)
    fun getActivitySummary(email: String): ActivitySummaryResponse {
        val admin = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (admin.role != UserRole.ADMIN) {
            throw IllegalArgumentException("ADMIN only")
        }

        val to = LocalDateTime.now()
        val from = to.minusDays(1)

        val signupCount = userRepository.countByCreatedAtAfter(from)
        val loginCount = loginHistoryRepository.countByCreatedAtAfter(from)
        val chatCount = chatRepository.countByCreatedAtAfter(from)

        return ActivitySummaryResponse(
            signupCount = signupCount,
            loginCount = loginCount,
            chatCount = chatCount,
            from = from,
            to = to
        )
    }
}
