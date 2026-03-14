package com.yyy.ai_chatbot_service.feedback.repository

import com.yyy.ai_chatbot_service.feedback.domain.Feedback
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByUser(user: User, pageable: Pageable): Page<Feedback>
    fun findByUserAndIsPositive(user: User, isPositive: Boolean, pageable: Pageable): Page<Feedback>
    fun findByIsPositive(isPositive: Boolean, pageable: Pageable): Page<Feedback>
    fun existsByUserIdAndChatId(userId: Long, chatId: Long): Boolean
}
