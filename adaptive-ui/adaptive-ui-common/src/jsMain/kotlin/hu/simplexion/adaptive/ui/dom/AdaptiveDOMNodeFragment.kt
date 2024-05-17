/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.dom

import hu.simplexion.adaptive.foundation.*
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.initStateMask
import hu.simplexion.adaptive.ui.css.AdaptiveCssStyle
import kotlinx.dom.hasClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

abstract class AdaptiveDOMNodeFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, index, stateSize) {

    abstract val receiver : Node

    @Suppress("UNCHECKED_CAST")
    fun getStyles(variableIndex : Int) =
        state[variableIndex] as? Array<out AdaptiveCssStyle> ?: emptyArray()

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
        check(fragment is AdaptiveDOMNodeFragment) { "invalid fragment type" } // TODO user ops
        receiver.appendChild(fragment.receiver)
    }

    override fun removeActual(fragment: AdaptiveFragment) {
        check(fragment is AdaptiveDOMNodeFragment) { "invalid fragment type" } // TODO user ops
        receiver.removeChild(fragment.receiver)
    }

}