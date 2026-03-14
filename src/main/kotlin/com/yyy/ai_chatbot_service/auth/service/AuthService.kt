package com.yyy.ai_chatbot_service.auth.service

import com.yyy.ai_chatbot_service.auth.domain.LoginHistory
import com.yyy.ai_chatbot_service.auth.dto.LoginRequest
import com.yyy.ai_chatbot_service.auth.dto.LoginResponse
import com.yyy.ai_chatbot_service.auth.dto.SignupRequest
import com.yyy.ai_chatbot_service.auth.dto.SignupResponse
import com.yyy.ai_chatbot_service.auth.jwt.JwtTokenProvider
import com.yyy.ai_chatbot_service.auth.repository.LoginHistoryRepository
import com.yyy.ai_chatbot_service.user.domain.User
import com.yyy.ai_chatbot_service.user.domain.UserRole
import com.yyy.ai_chatbot_service.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val loginHistoryRepository: LoginHistoryRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun signup(request: SignupRequest): SignupResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일입니다.")
        }

        val encodedPassword = passwordEncoder.encode(request.password)
        val user = User.create(
            email = request.email,
            password = encodedPassword,
            name = request.name,
            role = UserRole.MEMBER
        )

        val savedUser = userRepository.save(user)

        return SignupResponse(
            id = savedUser.id,
            email = savedUser.email,
            name = savedUser.name,
            role = savedUser.role
        )
    }

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.")
        }

        loginHistoryRepository.save(LoginHistory.create(user))

        val token = jwtTokenProvider.generateAccessToken(user.email, user.role)
        return LoginResponse(accessToken = token)
    }
}
