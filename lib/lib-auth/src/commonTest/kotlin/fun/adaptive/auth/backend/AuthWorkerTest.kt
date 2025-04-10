package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.backend.AuthTestSupport.Companion.authTest
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.value.firstItem
import `fun`.adaptive.value.firstItemOrNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthWorkerTest {

    @Test
    fun checkSecurityOfficerIsCreated() = authTest {
        val soRole = valueWorker.firstItem(AuthMarkers.ROLE) { AuthMarkers.SECURITY_OFFICER in it }
        assertEquals(authWorker.securityOfficer, soRole.uuid)

        val soPrincipal = valueWorker.firstItemOrNull(AuthMarkers.PRINCIPAL) { it.name == "so" }
        assertNotNull(soPrincipal)
    }

}