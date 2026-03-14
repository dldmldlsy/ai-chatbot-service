package com.yyy.ai_chatbot_service.chat.service

import com.yyy.ai_chatbot_service.chat.dto.ChatListResponse
import com.yyy.ai_chatbot_service.chat.dto.ChatSummaryResponse
import com.yyy.ai_chatbot_service.chat.dto.ThreadChatsResponse
import com.yyy.ai_chatbot_service.chat.repository.ChatRepository
import com.yyy.ai_chatbot_service.thread.repository.ChatThreadRepository
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatQueryService(
    private val userRepository: UserRepository,
    private val chatThreadRepository: ChatThreadRepository,
    private val chatRepository: ChatRepository
) {

    @Transactional(readOnly = true)
    fun getChats(email: String, page: Int, size: Int, direction: String): ChatListResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { IllegalArgumentException("User not found") }

        val sortDirection = if (direction.equals("asc", ignoreCase = true)) {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }

        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"))

        val threadsPage = when (user.role) {
            UserRole.ADMIN -> chatThreadRepository.findByDeletedAtIsNull(pageable)
            else -> chatThreadRepository.findByUserAndDeletedAtIsNull(user, pageable)
        }

        val threadIds = threadsPage.content.mapNotNull { it.id }
        if (threadIds.isEmpty()) {
            return ChatListResponse(
                content = emptyList(),
                page = threadsPage.number,
                size = threadsPage.size,
                totalElements = threadsPage.totalElements,
                totalPages = threadsPage.totalPages,
                hasNext = threadsPage.hasNext()
            )
        }

        val chats = chatRepository.findByThreadIdInOrderByCreatedAtAsc(threadIds)
        val chatsByThread = chats.groupBy { it.thread.id }

        val content = threadsPage.content.map { thread ->
            val summaries = chatsByThread[thread.id]?.map { chat ->
                ChatSummaryResponse(
                    chatId = chat.id ?: 0L,
                    question = chat.question,
                    answer = chat.answer,
                    model = chat.model,
                    isStreaming = chat.isStreaming,
                    createdAt = chat.createdAt
                )
            } ?: emptyList()

            ThreadChatsResponse(
                threadId = thread.id ?: 0L,
                userId = thread.user.id ?: 0L,
                createdAt = thread.createdAt,
                lastQuestionAt = thread.lastQuestionAt,
                chats = summaries
            )
        }

        return ChatListResponse(
            content = content,
            page = threadsPage.number,
            size = threadsPage.size,
            totalElements = threadsPage.totalElements,
            totalPages = threadsPage.totalPages,
            hasNext = threadsPage.hasNext()
        )
    }
}
