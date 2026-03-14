package com.yyy.ai_chatbot_service.thread.service

import com.yyy.ai_chatbot_service.thread.repository.ChatThreadRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ChatThreadCommandService(
    private val userRepository: UserRepository,
    private val chatThreadRepository: ChatThreadRepository
) {

    @Transactional
    fun deleteThread(email: String, threadId: Long) {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        val thread = chatThreadRepository.findByIdAndDeletedAtIsNull(threadId)
            .orElseThrow { IllegalArgumentException("Thread not found") }

        if (user.role != UserRole.ADMIN && thread.user.id != user.id) {
            throw IllegalArgumentException("삭제 권한이 없습니다.")
        }

        thread.delete(LocalDateTime.now())
        chatThreadRepository.save(thread)
    }
}
