package com.yyy.ai_chatbot_service.ai.client

import com.yyy.ai_chatbot_service.ai.config.OpenAiProperties
import com.yyy.ai_chatbot_service.ai.dto.OpenAiChatRequest
import com.yyy.ai_chatbot_service.ai.dto.OpenAiChatResponse
import com.yyy.ai_chatbot_service.ai.dto.OpenAiMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class OpenAiClient(
    private val properties: OpenAiProperties
) {

    private val log = LoggerFactory.getLogger(OpenAiClient::class.java)
    private val restClient: RestClient by lazy {
        RestClient.builder()
            .baseUrl(properties.baseUrl)
            .build()
    }

    private val fallbackAnswer: String =
        "OpenAI API Key가 설정되지 않아 실제 AI 응답 대신 기본 응답을 반환합니다."

    fun generateAnswer(messages: List<OpenAiMessage>, model: String): String {
        val apiKey = properties.apiKey
        if (apiKey.isNullOrBlank()) {
            log.info("OpenAI apiKey가 비어 있어 fallback 응답을 반환합니다.")
            return fallbackAnswer
        }

        return try {
            val request = OpenAiChatRequest(
                model = model,
                messages = messages
            )

            val response = restClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer $apiKey")
                .body(request)
                .retrieve()
                .toEntity(OpenAiChatResponse::class.java)

            val content = response.body
                ?.choices
                ?.firstOrNull()
                ?.message
                ?.content

            content ?: run {
                log.warn("OpenAI 응답에서 content를 찾지 못해 fallback 응답을 반환합니다.")
                fallbackAnswer
            }
        } catch (ex: Exception) {
            log.warn("OpenAI 호출 중 예외 발생, fallback 응답 반환: {}", ex.message)
            fallbackAnswer
        }
    }
}
