/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.DisplayMetrics
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.adapter.RenderData
import hu.simplexion.adaptive.ui.common.android.fragment.ViewFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.TextAlign

open class AdaptiveAndroidAdapter(
    val context: Context,
    override val rootContainer: ViewGroup
) : AdaptiveUIAdapter<AdaptiveViewGroup, View>() {

    override val fragmentFactory = ViewFragmentFactory

    val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    override fun makeContainerReceiver(fragment: AdaptiveUIContainerFragment<AdaptiveViewGroup, View>): AdaptiveViewGroup =
        AdaptiveViewGroup(context, fragment)

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AdaptiveUIContainerFragment<AdaptiveViewGroup, View>> {
            val frame = Frame(0f, 0f, rootContainer.width.toFloat(), rootContainer.height.toFloat())

            it.renderData.layoutFrame = frame
            it.measure()
            it.layout(frame)

            it.receiver.layoutParams = LinearLayout.LayoutParams(rootContainer.width, rootContainer.height)
            rootContainer.addView(it.receiver)
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.ifIsInstanceOrRoot<AdaptiveUIContainerFragment<AdaptiveViewGroup, View>> {
            rootContainer.removeView(it.receiver)
        }
    }

    override fun addActual(containerReceiver: AdaptiveViewGroup, itemReceiver: View) {
        containerReceiver.addView(itemReceiver)
    }

    override fun removeActual(itemReceiver: View) {
        (itemReceiver.parent as AdaptiveViewGroup).removeView(itemReceiver)
    }

    override fun applyLayoutToActual(fragment: AdaptiveUIFragment<View>) {
        val layoutFrame = fragment.renderData.layoutFrame

        check(layoutFrame !== Frame.NaF) { "Missing layout frame in $fragment $layoutFrame" }

        val point = layoutFrame.point
        val size = layoutFrame.size
        val top = point.top.toInt()
        val left = point.left.toInt()

        fragment.receiver.layoutParams = ViewGroup.LayoutParams(size.width.toInt(), size.height.toInt())
        fragment.receiver.layout(
            left,
            top,
            left + size.width.toInt(),
            top + size.height.toInt()
        )
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<View>) {
        with (fragment) {
            renderData = RenderData(instructions)
            // FIXME should clear actual UI settings when null

            if (renderData.tracePatterns.isNotEmpty()) {
                tracePatterns = renderData.tracePatterns
            }

            val view = receiver

            with(renderData) {

                val drawables = mutableListOf<Drawable>()
                val insets = mutableListOf<Int>()

                border?.let {
                    drawables += GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(android.graphics.Color.TRANSPARENT)
                        setStroke(it.width.toInt(), it.color.toAndroidColor())
                        borderRadius?.let { radius -> cornerRadius = radius }
                    }
                    insets += 0
                }

                backgroundColor?.let {
                    drawables += GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(it.toAndroidColor())
                        borderRadius?.let { radius -> cornerRadius = radius }
                    }
                    insets += border?.width?.toInt() ?: 0
                }

                backgroundGradient?.let {
                    drawables += GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, //
                        intArrayOf(it.start.toAndroidColor(), it.end.toAndroidColor()),
                    ).apply {
                        borderRadius?.let { radius -> cornerRadius = radius }
                    }
                    insets += border?.width?.toInt() ?: 0
                }

                if (drawables.isNotEmpty()) {
                    view.background = LayerDrawable(drawables.toTypedArray()).apply {
                        insets.forEachIndexed { index, inset ->
                            if (inset != 0) {
                                setLayerInset(index, inset, inset, inset, inset)
                            }
                        }
                    }
                }

                padding?.let {
                    view.setPadding(
                        it.left?.toInt() ?: 0,
                        it.top?.toInt() ?: 0,
                        it.right?.toInt() ?: 0,
                        it.bottom?.toInt() ?: 0
                    )
                }

                if (view is TextView) {
                    color?.let { view.setTextColor(it.toAndroidColor()) }

                    fontSize?.let { view.textSize = it }

                    fontWeight?.let {
                        // FIXME proper typeface mapping
                        if (it > 500) view.setTypeface(null, Typeface.BOLD)
                    }
                    letterSpacing?.let { view.letterSpacing = it }

                    when (textAlign) {
                        TextAlign.Start -> view.textAlignment = TEXT_ALIGNMENT_VIEW_START
                        TextAlign.Center -> view.textAlignment = TEXT_ALIGNMENT_CENTER
                        TextAlign.End -> view.textAlignment = TEXT_ALIGNMENT_VIEW_END
                        null -> Unit
                    }
                }

                onClick?.let {
                    view.setOnClickListener {
                        onClick !!.handler(AdaptiveUIEvent(fragment, it))
                    }
                }
            }
        }
    }

    fun pxToSp(px: Float): Float {
        return px / context.resources.displayMetrics.scaledDensity
    }
}