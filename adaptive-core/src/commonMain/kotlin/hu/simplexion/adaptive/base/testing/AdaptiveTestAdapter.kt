/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.testing

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveBridge
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.base.registry.AdaptiveBindingImplRegistry
import hu.simplexion.adaptive.base.registry.AdaptiveFragmentImplRegistry
import hu.simplexion.adaptive.utility.Lock
import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class AdaptiveTestAdapter(
    var printTrace : Boolean = false
) : AdaptiveAdapter<TestNode> {

    var nextId = 1L

    override val fragmentImplRegistry = AdaptiveFragmentImplRegistry<TestNode>()

    override lateinit var rootFragment: AdaptiveFragment<TestNode>

    override val rootBridge = AdaptiveTestBridge(newId())

    override var dispatcher: CoroutineDispatcher = Dispatchers.Main

    override val trace = true

    override val startedAt = vmNowMicro()

    val lock = getLock()

    val traceEvents = mutableListOf<TraceEvent>()

    var done: Boolean = false
        get() = lock.use { field }
        set(value) {
            lock.use { field = value }
        }

    override fun newId(): Long = nextId ++ // This is not thread safe, OK for testing, but beware.

    override fun createPlaceholder(): AdaptiveBridge<TestNode> {
        return AdaptiveTestBridge(newId())
    }

    override fun trace(fragment: AdaptiveFragment<TestNode>, point: String, data: String) {
        lock.use {
            traceEvents += TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data).also { if (printTrace) it.println(startedAt) }
        }
    }

    fun actual(dumpCode: Boolean = false): String =
        lock.use {
            traceEvents.joinToString("\n") { it.toActualString() }.also { if (dumpCode) println(toCode()) }
        }

    fun expected(expected: List<TraceEvent>): String =
        expected.joinToString("\n")

    suspend fun waitFor() {
        withTimeout(1000) {
            while (true) {
                if (done) break
                delay(10)
            }
        }
    }

    fun assert(expected: List<TraceEvent>): String {
        return if (expected == traceEvents) {
            "OK"
        } else {
            "Fail:\n==== expected ====\n${expected.joinToString("\n")}\n==== actual ====\n${traceEvents.joinToString("\n")}\n==== code ====\n${toCode()}"
        }
    }

    fun actual(): String =
        traceEvents.joinToString("\n")

    fun toCode(): String =
        traceEvents.joinToString(",\n") { it.toCode() }

}