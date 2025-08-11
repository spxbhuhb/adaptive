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

    @Test
    fun getOrCreateRoleTest() = authTest {
        val role = authWorker.getOrCreateRole("test", RoleSpec())
        assertEquals("test", role.name)

        val role2 = authWorker.getOrCreateRole("test", RoleSpec())
        assertEquals(role.uuid, role2.uuid)

        val role3 = valueWorker.get<RoleSpec>(AuthMarkers.ROLE).first { it.name == "test" }
        assertEquals(role.uuid, role3.uuid)
    }
}