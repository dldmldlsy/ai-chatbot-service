package com.yyy.ai_chatbot_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class AiChatbotServiceApplication

fun main(args: Array<String>) {
	runApplication<AiChatbotServiceApplication>(*args)
}
