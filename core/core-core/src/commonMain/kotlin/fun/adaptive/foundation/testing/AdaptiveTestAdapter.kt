/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.foundation.testing

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.query.filter
import `fun`.adaptive.service.testing.TestServiceTransport
import `fun`.adaptive.service.transport.ServiceCallTransport
import `fun`.adaptive.utility.PluginReference
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

class AdaptiveTestAdapter(
    var printTrace: Boolean = false,
    override val backend: BackendAdapter = BackendAdapter(
        transport = TestServiceTransport(),
        dispatcher = Dispatchers.Default,
        scope = CoroutineScope(Dispatchers.Default)
    )
) : AdaptiveAdapter {

    var nextId = 2L

    override val fragmentFactory = TestNodeFragmentFactory

    override val transport: ServiceCallTransport
        get() = backend.transport

    override lateinit var rootFragment: AdaptiveFragment

    override val rootContainer = TestNode()

    override val dispatcher: CoroutineDispatcher
        get() = backend.dispatcher

    override val scope = CoroutineScope(dispatcher)

    override var trace: Array<out Regex> = arrayOf(
        Regex(".*(?!addActual|removeActual)") // anything that does not end with addActual/ removeActual
    )
    override var traceWithContext = false

    override val startedAt = vmNowMicro()

    override fun addActualRoot(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveTestFragment) {
            rootContainer.appendChild(fragment.receiver)
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        if (fragment is AdaptiveTestFragment) {
            rootContainer.removeChild(fragment.receiver)
        }
    }

    val lock = getLock()

    val traceEvents = mutableListOf<TraceEvent>()

    var done: Boolean = false
        get() = lock.use { field }
        set(value) {
            lock.use { field = value }
        }

    override fun newId(): Long = nextId ++ // This is not thread safe, OK for testing, but beware.

    override fun trace(fragment: AdaptiveFragment, point: String, data: String) {
        if (trace.none { it.matches(point) }) return
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

    @Suppress("unused") // used by plugin tests
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

    val fails = mutableListOf<String>()

    @Suppress("unused") // used by plugin unit tests
    val checkResults: String
        get() =
            if (fails.isEmpty()) "OK" else "Fail:\n    ${fails.joinToString("\n    ")}\n"

    @Suppress("unused") // used by plugin unit tests
    fun checkInstructions(
        fragmentFilter: String,
        fragmentIndex: Int,
        vararg expected: AdaptiveInstruction
    ) {

        val fragments = filter(true) { fragmentFilter in it::class.simpleName !!.lowercase() }
        val actual = fragments[fragmentIndex].instructions

        if (actual.size != expected.size) fails += "instructions[$fragmentIndex].size != ${expected.size}"

        expected.forEachIndexed { index, instruction ->
            if (instruction !in actual) fails += "$instruction missing from fragment $fragmentIndex"
        }

        return
    }

    @Suppress("unused") // used by plugin unit tests
    fun changeState(
        fragmentFilter: String,
        fragmentIndex: Int,
        stateVariableIndex : Int,
        patch : Boolean = true,
        value : () -> Any?
    ) {
        val fragments = filter(true) { fragmentFilter in it::class.simpleName !!.lowercase() }
        val fragment = fragments[fragmentIndex]
        fragment.setStateVariable(stateVariableIndex, value())
        if (patch) fragment.setDirtyBatch(stateVariableIndex)
    }

    @Suppress("unused") // used by plugin unit tests
    val firstFragment : AdaptiveFragment
        get() = rootFragment.children.first()

}