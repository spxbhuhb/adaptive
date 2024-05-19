/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.browser.adapter

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.initStateMask
import deprecated.css.AdaptiveCssStyle
import kotlinx.dom.hasClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

abstract class AdaptiveBrowserFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, index, -1, stateSize) {

    abstract val receiver : Node

    @Suppress("UNCHECKED_CAST")
    fun getInstructions(variableIndex : Int) =
        state[variableIndex] as? Array<out AdaptiveInstruction> ?: emptyArray()

    fun getFragmentFactory(variableIndex : Int) =
        state[variableIndex] as BoundFragmentFactory

    val firstTimeInit
        get() = (dirtyMask == initStateMask)

    fun addClass(styles: Array<out AdaptiveCssStyle>) {
        with(receiver as HTMLElement) {
            val missingClasses = styles.filterNot { hasClass(it.name) }
            if (missingClasses.isNotEmpty()) {
                val presentClasses = className.trim()
                className = buildString {
                    append(presentClasses)
                    if (presentClasses.isNotEmpty()) {
                        append(" ")
                    }
                    missingClasses.joinTo(this, " ") { it.name }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun addActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveBrowserFragment) { "invalid fragment type" } // TODO user ops
        receiver.appendChild(fragment.receiver)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveBrowserFragment) { "invalid fragment type" } // TODO user ops
        receiver.removeChild(fragment.receiver)
    }

}