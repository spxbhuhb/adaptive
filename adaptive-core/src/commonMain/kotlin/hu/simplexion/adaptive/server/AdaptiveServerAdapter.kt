/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.server.builtin.AdaptiveService
import hu.simplexion.adaptive.utility.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Adapter for server applications.
 */
open class AdaptiveServerAdapter(
    wait : Boolean = false
) : AdaptiveAdapter {

    override val fragmentFactory = AdaptiveFragmentFactory()

    var nextId = 1L

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val rootContainer
        get() = throw NotImplementedError()

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Default

    override var trace = false

    // TODO implement cache synchronization

    val serviceCache = mutableMapOf<String,AdaptiveService>()

    val lock = getLock()

    var wait : Boolean = wait
        get() = lock.use { field }
        set(v) { lock.use { field = v } }

    override fun addActual(fragment: AdaptiveFragment, anchor : AdaptiveFragment?) {
        // there is no actual UI for server fragments
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        // there is no actual UI for server fragments
    }

    override fun newId(): Long =
        nextId ++

    inline fun <reified T> single(): T {
        // TODO create fragment.filterIsInstance
        val fragment = rootFragment.single { it is AdaptiveServerFragment && it.impl is T } as AdaptiveServerFragment
        val implementation = fragment.impl as? T
        return checkNotNull(implementation) { "fragment $fragment implementation is not set" }
    }

    open fun getLogger(name : String) = hu.simplexion.adaptive.log.getLogger(name)

    override fun mounted() {
        while (wait) {
            sleep(1000)
        }
    }

}