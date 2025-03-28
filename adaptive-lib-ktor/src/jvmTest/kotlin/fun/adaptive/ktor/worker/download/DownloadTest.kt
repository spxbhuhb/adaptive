package `fun`.adaptive.ktor.worker.download

import `fun`.adaptive.auth.api.AuthSessionApi
import `fun`.adaptive.auth.model.Credential
import `fun`.adaptive.auth.model.CredentialType
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ClientWebSocketServiceCallTransport
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.authJvm
import `fun`.adaptive.auth.util.BCrypt
import `fun`.adaptive.lib.auth.store.CredentialTable
import `fun`.adaptive.lib.auth.store.PrincipalTable
import `fun`.adaptive.reflect.CallSiteName
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.ensure
import `fun`.adaptive.utility.exists
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System.now
import kotlinx.io.files.Path
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DownloadTest {

    val testPath = Path("build/adaptive/test/ktor/download").ensure()

    fun server(dbName: String) = backend(TestServiceTransport()) {
        settings {
            inline("KTOR_WIREFORMAT" to "json")
            inline("KTOR_DOWNLOAD_DIR" to testPath.toString())
        }

        inMemoryH2(dbName)
        authJvm() // to have session worker
        ktor()
        service { FileService() }
    }

    @CallSiteName
    fun sessionTest(
        callSiteName: String = "unknown",
        test: suspend (adapter: BackendAdapter, session: Session) -> Unit,
    ) {
        val serverBackend = server(callSiteName.substringAfterLast('.'))
        val clientBackend = backend(webSocketTransport("http://localhost:8080")) { }.start()

        runBlocking {
            try {

                val admin = Principal(UUID(), "admin", activated = true)
                val passwd = BCrypt.hashpw("stuff", BCrypt.gensalt())

                transaction {
                    PrincipalTable.plusAssign(admin)
                    CredentialTable.plusAssign(Credential(UUID(), admin.id, CredentialType.PASSWORD, passwd, now()))
                }

                val session = getService<AuthSessionApi>(clientBackend.transport).login("admin", "stuff")
                delay(100) // let the websocket disconnect

                test(clientBackend, session)
            } finally {
                clientBackend.stop()
                serverBackend.stop()
            }
        }
    }

    @Test
    fun downloadTest() = sessionTest { adapter, session ->
        val service = getService<FileApi>(adapter.transport)

        val fileName = service.download()

        assertEquals("a.txt", fileName)
        assertTrue(Path(testPath, session.id.toString(), fileName).exists())

        val client = (adapter.transport as ClientWebSocketServiceCallTransport).client
        val response = client.get("http://localhost:8080/adaptive/download/$fileName")

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.bodyAsText()

        assertEquals("Hello World!", body)
    }

}