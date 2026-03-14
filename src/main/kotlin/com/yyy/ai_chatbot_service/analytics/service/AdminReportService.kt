package com.yyy.ai_chatbot_service.analytics.service

import com.yyy.ai_chatbot_service.chat.repository.ChatRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Service
class AdminReportService(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {

    @Transactional(readOnly = true)
    fun generateChatReport(email: String): ResponseEntity<ByteArray> {
        val admin = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (admin.role != UserRole.ADMIN) {
            throw IllegalArgumentException("ADMIN only")
        }

        val to = LocalDateTime.now()
        val from = to.minusDays(1)

        val chats = chatRepository.findByCreatedAtAfter(from)

        val builder = StringBuilder()
        builder.append("chatId,threadId,userId,userEmail,question,answer,model,isStreaming,createdAt\n")

        chats.forEach { chat ->
            val row = listOf(
                chat.id?.toString() ?: "",
                chat.thread.id?.toString() ?: "",
                chat.user.id?.toString() ?: "",
                chat.user.email,
                escapeCsv(chat.question),
                escapeCsv(chat.answer),
                chat.model,
                chat.isStreaming.toString(),
                chat.createdAt.toString()
            )
            builder.append(row.joinToString(",")).append("\n")
        }

        val bytes = builder.toString().toByteArray(StandardCharsets.UTF_8)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"chat-report.csv\"")
            .contentType(MediaType.TEXT_PLAIN)
            .body(bytes)
    }

    private fun escapeCsv(value: String?): String {
        if (value == null) return ""
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }
}
