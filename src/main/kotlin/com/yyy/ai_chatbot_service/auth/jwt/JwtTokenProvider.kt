package com.yyy.ai_chatbot_service.auth.jwt

import com.yyy.ai_chatbot_service.user.domain.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String
) {
    private val accessTokenValidityInMs: Long = 1000L * 60 * 60 * 24 // 24 hours

    private val signingKey: Key by lazy {
        Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateAccessToken(email: String, role: UserRole): String {
        val now = Date()
        val expiry = Date(now.time + accessTokenValidityInMs)

        return Jwts.builder()
            .setSubject(email)
            .claim("role", role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getEmail(token: String): String = parseClaims(token).subject

    fun getRole(token: String): UserRole? {
        val roleName = parseClaims(token).get("role", String::class.java)
        return roleName?.let { UserRole.valueOf(it) }
    }

    fun validateToken(token: String): Boolean =
        try {
            val claims = parseClaims(token)
            !claims.expiration.before(Date())
        } catch (_: JwtException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        }

    private fun parseClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(signingKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
