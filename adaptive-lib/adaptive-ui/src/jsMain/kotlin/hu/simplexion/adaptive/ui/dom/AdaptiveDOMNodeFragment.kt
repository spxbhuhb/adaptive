/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.dom

import hu.simplexion.adaptive.base.*
import hu.simplexion.adaptive.base.internal.BoundFragmentFactory
import hu.simplexion.adaptive.base.internal.initStateMask
import hu.simplexion.adaptive.ui.css.AdaptiveCssStyle
import kotlinx.dom.hasClass
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node

abstract class AdaptiveDOMNodeFragment(
    adapter: AdaptiveAdapter<Node>,
    parent: AdaptiveFragment<Node>?,
    index: Int,
    stateSize : Int,
    val leaf : Boolean
) : AdaptiveFragment<Node>(adapter, parent, index, stateSize), AdaptiveBridge<Node> {

    @Suppress("UNCHECKED_CAST")
    fun getStyles(variableIndex : Int) =
        state[variableIndex] as? Array<out AdaptiveCssStyle> ?: emptyArray()

    @Suppress("UNCHECKED_CAST")
    fun getFragmentFactory(variableIndex : Int) =
        state[variableIndex] as BoundFragmentFactory<Node>

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
    // Bridge overrides
    // -------------------------------------------------------------------------

    override fun remove(child: AdaptiveBridge<Node>) {
        check(!leaf)
        (receiver as HTMLElement).removeChild(child.receiver)
    }

    override fun replace(oldChild: AdaptiveBridge<Node>, newChild: AdaptiveBridge<Node>) {
        check(!leaf)
        throw IllegalStateException()
    }

    override fun add(child: AdaptiveBridge<Node>) {
        check(!leaf)
        (receiver as HTMLElement).appendChild(child.receiver)
    }

    // -------------------------------------------------------------------------
    // Fragment overrides
    // -------------------------------------------------------------------------

    override fun genBuild(parent: AdaptiveFragment<Node>, declarationIndex: Int): AdaptiveFragment<Node>? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment<Node>) = Unit

    override fun innerMount(bridge: AdaptiveBridge<Node>) {
        bridge.add(this)
        containedFragment?.mount(this)
    }

    override fun innerUnmount(bridge: AdaptiveBridge<Node>) {
        containedFragment?.unmount(this)
        bridge.remove(this)
    }

}