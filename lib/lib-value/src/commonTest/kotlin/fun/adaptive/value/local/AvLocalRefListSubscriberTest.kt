package `fun`.adaptive.value.local

import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment.FoundationLocalContext
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.foundation.testing.test
import `fun`.adaptive.service.api.getService
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.AvRefListSpec
import `fun`.adaptive.value.AvSubscribeCondition
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueApi
import `fun`.adaptive.value.embedded.EmbeddedValueServer.Companion.embeddedValueServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class AvLocalRefListSubscriberTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    @JsName("testProducerWithValueAlreadyInLocal")
    fun `test producer with value already in local`() = runTest {

        val valueId = uuid4<AvValue<*>>()
        val refId = uuid4<AvValue<*>>()

        val env = embeddedValueServer {
            addValue { AvValue(uuid = valueId, spec = AvRefListSpec(listOf(refId))) }
        }

        // this will pull the value from the remote into local
        // so we can test local subscription

        val service = getService<AvValueApi>(env.clientTransport)

        withContext(Dispatchers.Default.limitedParallelism(1)) {
            service.subscribe(listOf(AvSubscribeCondition(valueId)))
        }

        waitForReal(5.seconds) { env.clientWorker.getOrNull<AvRefListSpec>(valueId) != null }

        // this is the actual test, the value should be in the local
        // worker already

        val testAdapter = test(env.clientBackend) {
            val value = avLocalRefList(valueId)
            localContext(value) { }
        }

        waitForReal(5.seconds) {
            (testAdapter.firstOrNull<FoundationLocalContext>()?.context as? List<*>)?.firstOrNull() == refId
        }
    }

}