package `fun`.adaptive.value.local

import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment.FoundationLocalContext
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.asAvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class AcLocalValueSubscriberTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testProducerWithValueAlreadyInLocal")
    fun `test producer with value already in local`() = runTest {

        val valueId = uuid4<AvValue<*>>()

        val env = embeddedValueServer {
            addValue { AvValue(uuid = valueId, spec = 23) }
        }

        // this will pull the value from the remote into local
        // so we can test local subscription

        val service = getService<AvValueApi>(env.clientTransport)

        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // this is the actual test, the value should be in the local
        // worker already

        val testAdapter = test(env.clientBackend) {
            val value = avLocalValue(valueId, Int::class)
            localContext(value) { }
        }

        waitForReal(5.seconds) {
            testAdapter.firstOrNull<FoundationLocalContext>()?.context?.asAvValue<Int>()?.spec == 23
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testProducerWithValueNotInLocal")
    fun `test producer with value not in local`() = runTest {

        val valueId = uuid4<AvValue<*>>()

        val env = embeddedValueServer {
            addValue { AvValue(uuid = valueId, spec = 23) }
        }

        val testAdapter = test(env.clientBackend) {
            val value = avLocalValue(valueId, Int::class)
            localContext(value) { }
        }

        // the value should be null at this moment as it is not in the local store yet
        assertNull(testAdapter.firstOrNull<FoundationLocalContext>()?.context)

        val service = getService<AvValueApi>(env.clientTransport)

        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // when the value arrives to the local store, the fragment should update
        waitForReal(5.seconds) {
            testAdapter.firstOrNull<FoundationLocalContext>()?.context?.asAvValue<Int>()?.spec == 23
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testProducerValueChange")
    fun `test producer value change`() = runTest {

        val valueId = uuid4<AvValue<*>>()

        val env = embeddedValueServer {
            addValue { AvValue(uuid = valueId, spec = 23) }
        }

        // this will pull the value from the remote into local
        // so we can test local subscription

        val service = getService<AvValueApi>(env.clientTransport)

        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        waitForReal(5.seconds) { env.clientWorker.getOrNull<Int>(valueId) != null }

        // this is the actual test, the value should be in the local
        // worker already

        val testAdapter = test(env.clientBackend) {
            val value = avLocalValue(valueId, Int::class)
            localContext(value) { }
        }

        waitForReal(5.seconds) {
            testAdapter.firstOrNull<FoundationLocalContext>()?.context?.asAvValue<Int>()?.spec == 23
        }

        // update the value on the remote side, as a result, the fragment should update as well

        env.serverWorker.queueUpdate(AvValue(uuid = valueId, revision = 2L, spec = 42))

        waitForReal(5.seconds) {
            testAdapter.firstOrNull<FoundationLocalContext>()?.context?.asAvValue<Int>()?.spec == 42
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testProducerCleanup")
    fun `test producer cleanup`() = runTest {

        val valueId = uuid4<AvValue<*>>()

        val env = embeddedValueServer {
            addValue { AvValue(uuid = valueId, spec = 23) }
        }

        val testAdapter = test(env.clientBackend) {
            val value = avLocalValue(valueId, Int::class)
            localContext(value) { }
        }

        assertEquals(1, env.clientWorker.subscriptionCount(valueId))

        testAdapter.rootFragment.unmount()
        testAdapter.rootFragment.dispose()

        waitForReal(5.seconds) { env.clientWorker.subscriptionCount(valueId) == 0 }
    }
}