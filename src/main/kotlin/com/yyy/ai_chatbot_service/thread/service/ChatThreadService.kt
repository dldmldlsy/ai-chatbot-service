package com.yyy.ai_chatbot_service.thread.service

import com.yyy.ai_chatbot_service.thread.domain.ChatThread
import com.yyy.ai_chatbot_service.thread.repository.ChatThreadRepository
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class ChatThreadService(
    private val chatThreadRepository: ChatThreadRepository
) {

    private val reuseWindow: Duration = Duration.ofMinutes(30)

    @Transactional
    fun findOrCreateActiveThread(user: User): ChatThread {
        val now = LocalDateTime.now()
        val recentThread = chatThreadRepository.findFirstByUserAndDeletedAtIsNullOrderByLastQuestionAtDesc(user)

        val activeThread = if (recentThread != null && Duration.between(recentThread.lastQuestionAt, now) <= reuseWindow) {
            recentThread.updateLastQuestionAt(now)
            recentThread
        } else {
            ChatThread.create(user)
        }

        return chatThreadRepository.save(activeThread)
    }
}
