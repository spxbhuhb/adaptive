package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.backend.AuthTestSupport.Companion.authTest
import `fun`.adaptive.auth.model.AuthenticationFail
import `fun`.adaptive.auth.model.AuthenticationResult
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.api.getService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AuthSessionServiceTest {

    @Test
    fun signInUnknownPrincipal() = authTest {
        assertFailsWith(AuthenticationFail::class) {
            getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")
        }.also {
            assertEquals(AuthenticationResult.UnknownPrincipal, it.result)
        }
    }

    @Test
    fun signInNoCredentials() = authTest {

        addPrincipal("admin", null)

        assertFailsWith(AuthenticationFail::class) {
            getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")
        }.also {
            assertEquals(AuthenticationResult.NoCredential, it.result)
        }
    }

    @Test
    fun signInNotActivated() = authTest {
        addPrincipal("admin", "admin", PrincipalSpec(activated = false))

        assertFailsWith(AuthenticationFail::class) {
            getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")
        }.also {
            assertEquals(AuthenticationResult.NotActivated, it.result)
        }
    }

    @Test
    fun signInLocked() = authTest {
        addPrincipal("admin", "admin", PrincipalSpec(activated = true, locked = true))

        assertFailsWith(AuthenticationFail::class) {
            getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")
        }.also {
            assertEquals(AuthenticationResult.Locked, it.result)
        }
    }

    @Test
    fun signInSuccess() = authTest {
        val id = addPrincipal("admin", "admin", PrincipalSpec(activated = true, locked = false))

        val session = getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")
        assertNotNull(session.principalOrNull)
        assertEquals(id, session.principalOrNull)
    }

    @Test
    fun signOut() = authTest {
        addPrincipal("admin", "admin", PrincipalSpec(activated = true, locked = false))

        val session = getService<AuthSessionApi>(clientTransport).signIn("admin", "admin")

        serverTransport.skipDisconnect = true

        serverTransport.contextFun = {
            ServiceContext(serverTransport, session.uuid.cast(), session)
        }

        getService<AuthSessionApi>(clientTransport).signOut()
    }

}