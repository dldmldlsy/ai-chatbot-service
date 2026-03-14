package com.yyy.ai_chatbot_service.feedback.service

import com.yyy.ai_chatbot_service.chat.repository.ChatRepository
import com.yyy.ai_chatbot_service.feedback.domain.Feedback
import com.yyy.ai_chatbot_service.feedback.domain.FeedbackStatus
import com.yyy.ai_chatbot_service.feedback.dto.CreateFeedbackRequest
import com.yyy.ai_chatbot_service.feedback.dto.FeedbackResponse
import com.yyy.ai_chatbot_service.feedback.dto.UpdateFeedbackStatusRequest
import com.yyy.ai_chatbot_service.feedback.repository.FeedbackRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FeedbackCommandService(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val feedbackRepository: FeedbackRepository
) {

    @Transactional
    fun createFeedback(email: String, request: CreateFeedbackRequest): FeedbackResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        val chat = chatRepository.findById(request.chatId)
            .orElseThrow { IllegalArgumentException("Chat not found") }

        if (user.role != UserRole.ADMIN && chat.user.id != user.id) {
            throw IllegalArgumentException("해당 대화에 피드백을 남길 권한이 없습니다.")
        }

        if (feedbackRepository.existsByUserIdAndChatId(user.id ?: 0L, chat.id ?: 0L)) {
            throw IllegalArgumentException("이미 피드백을 작성했습니다.")
        }

        val feedback = Feedback.create(
            user = user,
            chat = chat,
            isPositive = request.isPositive
        )

        val saved = feedbackRepository.save(feedback)
        return saved.toResponse()
    }

    @Transactional
    fun updateStatus(email: String, feedbackId: Long, request: UpdateFeedbackStatusRequest): FeedbackResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (user.role != UserRole.ADMIN) {
            throw IllegalArgumentException("ADMIN만 상태를 변경할 수 있습니다.")
        }

        val feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow { IllegalArgumentException("Feedback not found") }

        val status = try {
            FeedbackStatus.valueOf(request.status.uppercase())
        } catch (ex: Exception) {
            throw IllegalArgumentException("Invalid status")
        }

        feedback.changeStatus(status)

        val saved = feedbackRepository.save(feedback)
        return saved.toResponse()
    }

    private fun Feedback.toResponse(): FeedbackResponse =
        FeedbackResponse(
            id = this.id ?: 0L,
            userId = this.user.id ?: 0L,
            chatId = this.chat.id ?: 0L,
            isPositive = this.isPositive,
            status = this.status,
            createdAt = this.createdAt
        )
}
