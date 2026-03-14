package com.yyy.ai_chatbot_service.thread.repository

import com.yyy.ai_chatbot_service.thread.domain.ChatThread
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ChatThreadRepository : JpaRepository<ChatThread, Long> {
    fun findFirstByUserAndDeletedAtIsNullOrderByLastQuestionAtDesc(user: User): ChatThread?
    fun findByUserAndDeletedAtIsNull(user: User, pageable: Pageable): Page<ChatThread>
    fun findByDeletedAtIsNull(pageable: Pageable): Page<ChatThread>
    fun findByIdAndDeletedAtIsNull(id: Long): Optional<ChatThread>
}
