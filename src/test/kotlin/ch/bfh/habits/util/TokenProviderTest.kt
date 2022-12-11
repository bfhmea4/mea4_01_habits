package ch.bfh.habits.util

import org.junit.Test
import org.springframework.security.core.userdetails.User as SpringUser
import ch.bfh.habits.entities.User
import junit.framework.TestCase.assertTrue
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.assertFalse

class TokenProviderTest {
    private val tokenProvider = TokenProvider()

    init {
        tokenProvider.signingKey = "test"
        tokenProvider.tokenValidity = 604800
        tokenProvider.tokenPrefix = "Bearer"
    }

    @Test
    fun `generateToken should return a JWT string`() {
        // when
        val jwt = tokenProvider.generateToken(
            SpringUser("test", "test", emptyList()),
            User("test", "test", "test", "test@test.com", 1)
        )

        // then
        assert(jwt.isNotEmpty())
    }

    @Test
    fun `validateToken should return true if token is valid`() {
        // given
        val userDetails = SpringUser("test", "test", emptyList())
        val user = User("test", "test", "test", "test@test.com", 1)
        val jwt = tokenProvider.generateToken(userDetails, user)

        // when
        val isValid = tokenProvider.validateToken(jwt, userDetails)

        // then
        assertTrue(isValid)
    }

    @Test
    fun `validateToken should return false if token is not valid`() {
        // given
        val userDetails = SpringUser("test", "test", emptyList())
        val user = User("test", "test", "test", "test@test.com", 1)
        val jwt = tokenProvider.generateToken(userDetails, user)

        // when
        val isValid = tokenProvider.validateToken(jwt, SpringUser("wrong", "test", emptyList()))

        // then
        assertFalse(isValid)
    }

    @Test
    fun `validateToken should return false if token is expired`() {
        // given
        val tokenProviderTest = TokenProvider()
        tokenProviderTest.signingKey = "test"
        tokenProviderTest.tokenValidity = 0
        tokenProviderTest.tokenPrefix = "Bearer"

        val userDetails = SpringUser("test", "test", emptyList())
        val user = User("test", "test", "test", "test@test.com", 1)
        val jwt = tokenProviderTest.generateToken(userDetails, user)

        // when
        val isValid = tokenProviderTest.validateToken(jwt, SpringUser("test", "test", emptyList()))

        // then
        assertFalse(isValid)
    }

    @Test
    fun `extractUsername should return the username from the token`() {
        // given
        val userDetails = SpringUser("test", "test", emptyList())
        val user = User("test", "test", "test", "test@test.com", 1)
        val jwt = tokenProvider.generateToken(userDetails, user)

        // when
        val userName = tokenProvider.extractUsername(jwt)

        // then
        assertThat(userName).isEqualTo("test")
    }

    @Test
    fun `extractId should return the user id from the token`() {
        // given
        val userDetails = SpringUser("test", "test", emptyList())
        val user = User("test", "test", "test", "test@test.com", 1)
        val jwt = tokenProvider.generateToken(userDetails, user)

        // when
        val id = tokenProvider.extractId(jwt)

        // then
        assertThat(id).isEqualTo(1L)
    }
}
