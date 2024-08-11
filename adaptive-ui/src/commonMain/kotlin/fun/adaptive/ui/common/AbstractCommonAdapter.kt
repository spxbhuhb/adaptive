/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common

import `fun`.adaptive.foundation.*
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.ui.common.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.common.fragment.structural.CommonLoop
import `fun`.adaptive.ui.common.fragment.structural.CommonSelect
import `fun`.adaptive.ui.common.instruction.Color
import `fun`.adaptive.ui.common.instruction.DPixel
import `fun`.adaptive.ui.common.instruction.FontWeight
import `fun`.adaptive.ui.common.instruction.SPixel
import `fun`.adaptive.ui.common.platform.MediaMetrics
import `fun`.adaptive.ui.common.platform.MediaMetricsProducer
import `fun`.adaptive.ui.common.render.CommonRenderData
import `fun`.adaptive.ui.common.render.TextRenderData
import `fun`.adaptive.ui.common.support.navigation.AbstractNavSupport
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Container receiver types ([CRT]):
 *
 * - android: AdaptiveViewGroup
 * - ios: UIView
 * - browser: HTMLDivElement
 *
 * Receiver types ([RT]):
 *
 * - android: View
 * - ios: UIView
 * - browser: HTMLElement
 *
 * @param CRT Container receiver type
 * @param RT Receiver type
 *
 * @property  autoSizing   When `true`, browser layouts will use the browsers built-in layout methods.
 *                         When `false` they will use the same algorithm as mobile. This seconds option
 *                         is useful when you use browsers to develop for mobile.
 *
 * @property  actualBatchOwner  Used by layout fragments to group remove operations, so they can remove
 *                              a whole actual subtree at once.
 */
abstract class AbstractCommonAdapter<RT, CRT : RT> : AdaptiveAdapter {

    var nextId = 1L

    override var trace: Array<out Regex> = emptyArray()

    override fun newId(): Long =
        nextId ++

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    var autoSizing: Boolean = true

    var actualBatchOwner: AbstractContainer<*, *>? = null

    val emptyRenderData = CommonRenderData(this)

    var defaultTextRenderData = TextRenderData().apply {
        color = Color(0u)
        fontSize = SPixel(17.0)
        fontWeight = FontWeight.NORMAL.weight
    }

    override fun newSelect(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        CommonSelect(this, parent, index)

    override fun newLoop(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        CommonLoop(this, parent, index)

    abstract fun makeContainerReceiver(fragment: AbstractContainer<RT, CRT>): CRT

    abstract fun makeStructuralReceiver(fragment: AbstractContainer<RT, CRT>): CRT

    fun traceAddActual(fragment: AdaptiveFragment) {
        if (trace.isEmpty()) return
        trace("addActual", "fragment: $fragment")
    }

    fun traceRemoveActual(fragment: AdaptiveFragment) {
        if (trace.isEmpty()) return
        trace("removeActual", "fragment: $fragment")
    }

    /**
     * Layouts call [addActual] to perform whatever platform call is needed
     * to add the item to the container on actual UI level.
     */
    abstract fun addActual(containerReceiver: CRT, itemReceiver: RT)

    /**
     * Layouts call [removeActual] to perform whatever platform call is needed
     * to remove the item from the container on actual UI level.
     */
    abstract fun removeActual(itemReceiver: RT)

    /**
     * Perform the layout on the actual UI.
     */
    abstract fun applyLayoutToActual(fragment: AbstractCommonFragment<RT>)

    /**
     * Apply render instructions to the fragment such as coloring, border etc.
     */
    abstract fun applyRenderInstructions(fragment: AbstractCommonFragment<RT>)

    inline fun <reified T> AdaptiveFragment.ifIsInstanceOrRoot(block: (it: T) -> Unit) {
        if (this is T) {
            block(this)
        } else {
            opsCheck(this == rootFragment, nonLayoutTopLevelPath) { nonLayoutTopLevelMessage }
        }
    }

    open fun openExternalLink(href: String) {
        throw UnsupportedOperationException("openExternalLink($href)")
    }

    // ------------------------------------------------------------------------------
    // Device independent pixel <-> device dependent pixel conversions
    // ------------------------------------------------------------------------------

    abstract fun toPx(dPixel: DPixel): Double

    abstract fun toDp(value: Double): DPixel

    abstract fun toPx(sPixel: SPixel): Double

    // ------------------------------------------------------------------------------
    // Media metrics support
    // ------------------------------------------------------------------------------

    abstract var mediaMetrics: MediaMetrics

    var manualTheme: ThemeQualifier? = null
        set(v) {
            field = v
            mediaMetrics = mediaMetrics.copy(manualTheme = v)
            updateMediaMetrics()
        }

    protected val mediaMetricsProducers = mutableListOf<MediaMetricsProducer>()

    protected fun updateMediaMetrics() {
        mediaMetricsProducers.forEach { it.update(mediaMetrics) }
    }

    /**
     * Add a media metrics producer that will be notified of media changes.
     */
    open fun addMediaMetricsProducer(producer: MediaMetricsProducer) {
        mediaMetricsProducers += producer
    }

    /**
     * Remove a previously added media metrics producer.
     */
    open fun removeMediaMetricsProducer(producer: MediaMetricsProducer) {
        mediaMetricsProducers -= producer
    }

    // ------------------------------------------------------------------------------
    // Navigation support
    // ------------------------------------------------------------------------------

    abstract val navSupport: AbstractNavSupport

    fun navChange() {
        trace("nav-change", navSupport.root.toString())
    }
}