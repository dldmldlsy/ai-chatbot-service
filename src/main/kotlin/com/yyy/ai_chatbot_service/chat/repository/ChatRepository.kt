package com.yyy.ai_chatbot_service.chat.repository

import com.yyy.ai_chatbot_service.chat.domain.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : JpaRepository<Chat, Long>
