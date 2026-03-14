package com.yyy.ai_chatbot_service.chat.repository

import com.yyy.ai_chatbot_service.chat.domain.Chat
import com.yyy.ai_chatbot_service.thread.domain.ChatThread
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ChatRepository : JpaRepository<Chat, Long> {
    fun findByThreadOrderByCreatedAtAsc(thread: ChatThread): List<Chat>
    fun findByThreadIdInOrderByCreatedAtAsc(threadIds: List<Long>): List<Chat>
    fun findById(id: Long): Optional<Chat>
}
