package `fun`.adaptive.auth.backend

import `fun`.adaptive.auth.backend.AuthTestSupport.Companion.authTest
import `fun`.adaptive.auth.model.AuthMarkers
import `fun`.adaptive.auth.model.PrincipalSpec
import `fun`.adaptive.auth.model.RoleSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthWorkerTest {

    @Test
    fun checkSecurityOfficerIsCreated() = authTest {
        val soRole = valueWorker.get<RoleSpec>(AuthMarkers.ROLE).first { AuthMarkers.SECURITY_OFFICER in it.markers }
        assertEquals(authWorker.securityOfficer, soRole.uuid)

        val soPrincipal = valueWorker.get<PrincipalSpec>(AuthMarkers.PRINCIPAL).firstOrNull { it.name == "so" }
        assertNotNull(soPrincipal)
    }

}