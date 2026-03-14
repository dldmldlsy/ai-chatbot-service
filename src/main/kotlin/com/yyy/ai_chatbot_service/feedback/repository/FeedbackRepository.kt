package com.yyy.ai_chatbot_service.feedback.repository

import com.yyy.ai_chatbot_service.feedback.domain.Feedback
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long>
