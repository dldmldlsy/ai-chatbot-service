package com.yyy.ai_chatbot_service.thread.repository

import com.yyy.ai_chatbot_service.thread.domain.ChatThread
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatThreadRepository : JpaRepository<ChatThread, Long> {
    fun findFirstByUserAndDeletedAtIsNullOrderByLastQuestionAtDesc(user: User): ChatThread?
}
