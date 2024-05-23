/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class AdaptiveUIAdapter : AdaptiveAdapter {

    var nextId = 1L

    override fun newId(): Long =
        nextId ++

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    fun traceAddActual(fragment: AdaptiveFragment) {
        if (trace) trace("addActual", "fragment: $fragment")
    }

    fun traceRemoveActual(fragment: AdaptiveFragment) {
        if (trace) trace("removeActual", "fragment: $fragment")
    }

    inline fun <reified T> AdaptiveFragment.ifIsInstanceOrRoot(block : (it : T) -> Unit) {
        if (this is T) {
            block(this)
        } else {
            opsCheck(this == rootFragment, nonLayoutTopLevelPath) { nonLayoutTopLevelMessage }
        }
    }

}