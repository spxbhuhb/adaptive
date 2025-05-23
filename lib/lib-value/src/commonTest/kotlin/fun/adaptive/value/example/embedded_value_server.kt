package `fun`.adaptive.value.example

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.withEmbeddedValueServer
import `fun`.adaptive.value.persistence.FilePersistence
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.seconds

//@example
fun createExample() {
    embeddedValueServer { }
}

//@example
fun initExample() {
    embeddedValueServer {
        addValue { AvValue(spec = "Hello World!") }
        addValue { AvValue(spec = "Hello World 2!") }
    }
}

//@example
fun persistenceExample() {
    embeddedValueServer(FilePersistence(Path("test"), 2)) {
        addValue { AvValue(spec = "Hello World!") }
        addValue { AvValue(spec = "Hello World 2!") }
    }
}

//@example
fun accessExample() {
    embeddedValueServer { }.apply {
        serverWorker.queryByMarker("testMarker").forEach { println(it) }
    }
}

//@example
suspend fun executeExample() {
    embeddedValueServer { }.execute {
        addValue { AvValue(spec = "Hello World!") }
    }
}

//@example
fun testExample() = runTest {

    withEmbeddedValueServer {
        serverWorker.queueAdd(AvValue(markersOrNull = setOf("testMarker"), spec = "test"))
        waitForIdle(1.seconds)
        serverWorker.queryByMarker("testMarker").forEach { println(it) }
    }

}