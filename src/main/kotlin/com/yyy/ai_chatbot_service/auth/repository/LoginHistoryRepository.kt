package com.yyy.ai_chatbot_service.auth.repository

import com.yyy.ai_chatbot_service.auth.domain.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LoginHistoryRepository : JpaRepository<LoginHistory, Long>
{
    fun countByCreatedAtAfter(createdAt: LocalDateTime): Long
}
