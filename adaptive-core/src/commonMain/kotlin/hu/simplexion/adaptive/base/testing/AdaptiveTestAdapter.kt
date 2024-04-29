/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.base.testing

import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveBridge
import hu.simplexion.adaptive.base.AdaptiveFragment
import hu.simplexion.adaptive.utility.Lock
import hu.simplexion.adaptive.utility.use
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class AdaptiveTestAdapter : AdaptiveAdapter<TestNode> {

    var nextId = 1L

    override val trace = true

    override fun newId(): Long = nextId ++ // This is not thread safe, OK for testing, but beware.

    override lateinit var rootFragment: AdaptiveFragment<TestNode>

    override val rootBridge = AdaptiveTestBridge(newId())

    override var dispatcher: CoroutineDispatcher = Dispatchers.Main

    val lock = Lock()

    val traceEvents = mutableListOf<TraceEvent>()

    override val startedAt = vmNowMicro()

    var done: Boolean = false
        get() = lock.use { field }
        set(value) {
            lock.use { field = value }
        }

    init {
        lastTrace = traceEvents
    }

    override fun createPlaceholder(): AdaptiveBridge<TestNode> {
        return AdaptiveTestBridge(newId())
    }

    override fun trace(fragment: AdaptiveFragment<TestNode>, point: String, data: String) {
        lock.use {
            traceEvents += TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data) //.println(startedAt)
        }
    }

    fun actual(dumpCode: Boolean = false): String =
        lock.use {
            traceEvents.joinToString("\n").also { if (dumpCode) println(toCode()) }
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

    companion object {
        // Unit tests use this property when they run the generated fragment.
        // The trace of the last created adapter is here, unit tests should
        // clear this field before running the generated code.
        var lastTrace: MutableList<TraceEvent> = mutableListOf()

        fun actual(): String =
            lastTrace.joinToString("\n")

        fun expected(expected: List<TraceEvent>): String =
            expected.joinToString("\n")

        fun toCode(): String =
            lastTrace.joinToString(",\n") { it.toCode() }

        fun assert(expected: List<TraceEvent>): String {
            return if (expected == lastTrace) {
                "OK"
            } else {
                "Fail:\n==== expected ====\n${expected.joinToString("\n")}\n==== actual ====\n${lastTrace.joinToString("\n")}\n==== code ====\n${toCode()}"
            }
        }
    }
}