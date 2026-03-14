package com.yyy.ai_chatbot_service.chat.service

import com.yyy.ai_chatbot_service.ai.client.OpenAiClient
import com.yyy.ai_chatbot_service.ai.config.OpenAiProperties
import com.yyy.ai_chatbot_service.ai.dto.OpenAiMessage
import com.yyy.ai_chatbot_service.chat.domain.Chat
import com.yyy.ai_chatbot_service.chat.dto.CreateChatRequest
import com.yyy.ai_chatbot_service.chat.dto.CreateChatResponse
import com.yyy.ai_chatbot_service.chat.repository.ChatRepository
import com.yyy.ai_chatbot_service.thread.service.ChatThreadService
import com.yyy.ai_chatbot_service.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val chatThreadService: ChatThreadService,
    private val openAiClient: OpenAiClient,
    private val openAiProperties: OpenAiProperties
) {

    @Transactional
    fun createChat(currentUser: User, request: CreateChatRequest): CreateChatResponse {
        val thread = chatThreadService.findOrCreateActiveThread(currentUser)
        val previousChats = chatRepository.findByThreadOrderByCreatedAtAsc(thread)

        val messages = mutableListOf<OpenAiMessage>()
        messages.add(OpenAiMessage(role = "system", content = "You are a helpful assistant."))

        previousChats.forEach { chat ->
            messages.add(OpenAiMessage(role = "user", content = chat.question))
            messages.add(OpenAiMessage(role = "assistant", content = chat.answer))
        }

        messages.add(OpenAiMessage(role = "user", content = request.question))

        val modelToUse = request.model ?: openAiProperties.model
        val answer = openAiClient.generateAnswer(messages, modelToUse)

        val chat = Chat.create(
            thread = thread,
            user = currentUser,
            question = request.question,
            answer = answer,
            model = modelToUse,
            isStreaming = request.isStreaming
        )

        val savedChat = chatRepository.save(chat)

        return CreateChatResponse(
            threadId = thread.id,
            chatId = savedChat.id,
            question = savedChat.question,
            answer = savedChat.answer,
            model = savedChat.model,
            isStreaming = savedChat.isStreaming,
            createdAt = savedChat.createdAt
        )
    }
}
