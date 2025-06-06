/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.FragmentTask
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.nonLayoutTopLevelMessage
import `fun`.adaptive.foundation.nonLayoutTopLevelPath
import `fun`.adaptive.foundation.opsCheck
import `fun`.adaptive.log.expectNotNull
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.resource.ThemeQualifier
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.api.normalFont
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.fragment.structural.AuiLoop
import `fun`.adaptive.ui.fragment.structural.AuiSelect
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.layout.Alignment
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.platform.media.MediaMetricsProducer
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.render.model.TextRenderData
import `fun`.adaptive.ui.support.navigation.AbstractNavSupport
import `fun`.adaptive.utility.trimSignature
import `fun`.adaptive.utility.vmNowMicro
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

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
 *
 * @property  afterClosePatchBatch   When not null, the adapter calls this function after closing a patch batch.
 */
abstract class AbstractAuiAdapter<RT, CRT : RT> : DensityIndependentAdapter() {

    var nextId = 1L

    override var trace: Array<out Regex> = emptyArray()

    override var application: AbstractApplication<*,*>? = null

    override fun newId(): Long =
        nextId ++

    override var traceWithContext = false

    override val startedAt = vmNowMicro()

    override lateinit var rootFragment: AdaptiveFragment

    override val dispatcher: CoroutineDispatcher
        get() = Dispatchers.Main

    override val scope = CoroutineScope(dispatcher)

    val logger = getLogger(this.typeSignature().trimSignature().substringAfterLast('.'))

    open val scrollBarSize: Double = 0.0

    var autoSizing: Boolean = true

    var actualBatchOwner: AbstractContainer<*, *>? = null

    var updateBatchId = 1L

    val updateBatch = mutableListOf<AbstractAuiFragment<RT>>()

    val emptyRenderData = AuiRenderData(this)

    var afterClosePatchBatch: ((adapter: AbstractAuiAdapter<RT, CRT>) -> Unit)? = null

    var defaultTextRenderData = TextRenderData().apply {
        color = Color(0u)
        fontName = "sans-serif"
        fontSize = SPixel(17.0)
        fontWeight = normalFont.weight
    }

    override fun stop() {
        scope.cancel()
    }

    override fun newSelect(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        AuiSelect(this, parent, index)

    override fun newLoop(parent: AdaptiveFragment, index: Int): AdaptiveFragment =
        AuiLoop(this, parent, index)

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

    val afterPatchBatchTasks = mutableListOf<FragmentTask>()

    fun addAfterPatchBatchTask(task: FragmentTask) {
        afterPatchBatchTasks += task
    }

    override fun closePatchBatch() {
        val updateId = ++ updateBatchId
        updateBatchId ++

        for (fragment in updateBatch) {
            fragment.updateLayout(updateId, null)
        }

        updateBatch.clear()
        afterClosePatchBatch?.invoke(this)

        afterPatchBatchTasks.forEach { it.execute() }
        afterPatchBatchTasks.clear()
    }

    /**
     * Perform the layout on the actual UI.
     */
    abstract fun applyLayoutToActual(fragment: AbstractAuiFragment<RT>)

    /**
     * Apply render instructions to the fragment such as coloring, border etc.
     */
    abstract fun applyLayoutIndependent(fragment: AbstractAuiFragment<RT>)

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
    // Theme support
    // ------------------------------------------------------------------------------

    val theme = mutableMapOf<String, AdaptiveInstructionGroup>()

    fun themeFor(fragment: AbstractAuiFragment<RT>): AdaptiveInstructionGroup =
        theme[fragment::class.simpleName] ?: emptyInstructions

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
    // Scroll support
    // ------------------------------------------------------------------------------


    /**
     * Get the scroll position of the fragment in the nearest scrollable
     * layout container.
     */
    fun scrollState(fragment : AbstractAuiFragment<RT>) : Pair<AbstractContainer<RT,CRT>?,RawPosition?> {
        val renderData = fragment.renderData

        var top = renderData.finalTop
        var left = renderData.finalLeft

        var currentContainer = renderData.layoutFragment

        while (currentContainer != null) {
            val currentRenderData = currentContainer.renderData
            val v = currentRenderData.container?.verticalScroll ?: false
            val h = currentRenderData.container?.horizontalScroll ?: false

            if (v || h) {
                @Suppress("UNCHECKED_CAST")
                return (currentContainer as AbstractContainer<RT, CRT> to RawPosition(top, left))
            }

            val scrollPosition = scrollPosition(currentContainer) !!
            top += currentRenderData.finalTop - scrollPosition.top
            left += currentRenderData.finalLeft - scrollPosition.left
            currentContainer = currentRenderData.layoutFragment
        }

        return null to RawPosition(top, left)
    }

    open fun scrollPosition(fragment: AdaptiveFragment): RawPosition? {
        throw UnsupportedOperationException("scrollPosition($fragment)")
    }

    open fun scrollTo(fragment: AdaptiveFragment, position: RawPosition) {
        throw UnsupportedOperationException("scrollTo($fragment, $position)")
    }

    open fun scrollIntoView(fragment: AdaptiveFragment, alignment: Alignment) {
        throw UnsupportedOperationException("scrollIntoView($fragment)")
    }

    inline fun <reified RT> expectUiFragment(fragment: AdaptiveFragment): AbstractAuiFragment<RT>? {
        @Suppress("UNCHECKED_CAST")
        return expectNotNull(logger, fragment as? AbstractAuiFragment<RT>) { "Fragment is not an AuiFragment: ${fragment.typeSignature()}" }
    }

    // ------------------------------------------------------------------------------
    // Navigation support
    // ------------------------------------------------------------------------------

    abstract val navSupport: AbstractNavSupport

    fun navChange() {
        trace("nav-change", navSupport.root.toString())
    }

    // ------------------------------------------------------------------------------
    // Hit detection
    // ------------------------------------------------------------------------------


    /**
     * Finds all descendant fragments of [start] at the given position.
     *
     * Does not add structural fragments to the result.
     *
     * @param start The fragment to start the search from
     * @param x The horizontal position to search at, use NaN to ignore check
     * @param y The vertical position to search at, use NaN to ignore check
     * @return List of fragments that contain the given position, ordered from outermost to innermost
     */
    fun findByPosition(
        start: AbstractAuiFragment<*>,
        x: Double,
        y: Double
    ): List<AbstractAuiFragment<*>> {
        val result = mutableListOf<AbstractAuiFragment<*>>()
        for (child in start.children) {
            findByPosition(child, y, x, result)
        }
        return result
    }

    private fun findByPosition(
        fragment: AdaptiveFragment,
        top: Double,
        left: Double,
        result: MutableList<AbstractAuiFragment<*>>
    ) {
        if (fragment is AbstractAuiFragment<*>) {

            val renderData = fragment.renderData

            val finalTop = renderData.finalTop
            val finalLeft = renderData.finalLeft

            val isInside =
                left.isNaN() || (left >= finalLeft && left <= finalLeft + renderData.finalWidth) &&
                top.isNaN() || (top >= finalTop && top <= finalTop + renderData.finalHeight)

            if (isInside) {
                if (!fragment.isStructural) {
                    @Suppress("UNCHECKED_CAST")
                    result.add(fragment as AbstractAuiFragment<RT>)
                }
                for (child in fragment.children) {
                    findByPosition(child, top - finalTop, left - finalLeft, result)
                }
            }

        } else {
            for (child in fragment.children) {
                findByPosition(child, top, left, result)
            }
        }
    }

}
