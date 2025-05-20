package `fun`.adaptive.value.testing

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.testing.EmbeddedValueServer.Companion.withEmbeddedValueServer
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.seconds

/**
 * Demonstrates the use of [EmbeddedValueServer]. This example
 * uses [runTest] because client - server communication is
 * asynchronous by nature.
 */
fun valueTestExample() = runTest {

    withEmbeddedValueServer {
        serverWorker.queueAdd(AvValue(markersOrNull = setOf("testMarker"), spec = "test"))
        waitForIdle(1.seconds)
        serverWorker.queryByMarker("testMarker").forEach { println(it) }
    }

}