package `fun`.adaptive.ktor.worker.download

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.ktor.ClientWebSocketServiceCallTransport
import `fun`.adaptive.ktor.api.webSocketTransport
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.utility.clearedTestPath
import `fun`.adaptive.utility.ensure
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import org.junit.Test
import kotlin.test.assertEquals

class DownloadTest {

// FIXME KTOR download test - needs a session service
//    fun server(path: Path) = backend(TestServiceTransport()) {
//        settings {
//            inline("KTOR_WIREFORMAT" to "json")
//            inline("KTOR_DOWNLOAD_DIR" to path.toString())
//        }
//
//        ktor()
//        service { FileService() }
//    }
//
//    fun sessionTest(
//        path : Path,
//        test: suspend (adapter: BackendAdapter) -> Unit,
//    ) {
//        val serverBackend = server(path)
//        val clientBackend = backend(webSocketTransport("http://localhost:8080")) { }.start()
//
//        runBlocking {
//            try {
//                test(clientBackend)
//            } finally {
//                clientBackend.stop()
//                serverBackend.stop()
//            }
//        }
//    }
//
//    @Test
//    fun downloadTest() = sessionTest(clearedTestPath()) { adapter ->
//        val service = getService<FileApi>(adapter.transport)
//
//        val fileName = service.download()
//
//        assertEquals("a.txt", fileName)
//        //assertTrue(Path(testPath, session.uuid.toString(), fileName).exists())
//
//        val client = (adapter.transport as ClientWebSocketServiceCallTransport).client
//        val response = client.get("http://localhost:8080/adaptive/download/$fileName")
//
//        assertEquals(HttpStatusCode.OK, response.status)
//
//        val body = response.bodyAsText()
//
//        assertEquals("Hello World!", body)
//    }

}