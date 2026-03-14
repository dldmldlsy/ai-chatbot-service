package com.yyy.ai_chatbot_service.feedback.service

import com.yyy.ai_chatbot_service.feedback.dto.FeedbackListResponse
import com.yyy.ai_chatbot_service.feedback.dto.FeedbackResponse
import com.yyy.ai_chatbot_service.feedback.repository.FeedbackRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackQueryService(
    private val userRepository: UserRepository,
    private val feedbackRepository: FeedbackRepository
) {

    @Transactional(readOnly = true)
    fun getFeedbacks(
        email: String,
        page: Int,
        size: Int,
        direction: String,
        isPositive: Boolean?
    ): FeedbackListResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        val sortDirection = if (direction.equals("asc", ignoreCase = true)) {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }

        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"))

        val feedbackPage = when (user.role) {
            UserRole.ADMIN -> {
                if (isPositive == null) feedbackRepository.findAll(pageable)
                else feedbackRepository.findByIsPositive(isPositive, pageable)
            }
            else -> {
                if (isPositive == null) feedbackRepository.findByUser(user, pageable)
                else feedbackRepository.findByUserAndIsPositive(user, isPositive, pageable)
            }
        }

        val content = feedbackPage.content.map {
            FeedbackResponse(
                id = it.id ?: 0L,
                userId = it.user.id ?: 0L,
                chatId = it.chat.id ?: 0L,
                isPositive = it.isPositive,
                status = it.status,
                createdAt = it.createdAt
            )
        }

        return FeedbackListResponse(
            content = content,
            page = feedbackPage.number,
            size = feedbackPage.size,
            totalElements = feedbackPage.totalElements,
            totalPages = feedbackPage.totalPages,
            hasNext = feedbackPage.hasNext()
        )
    }
}
