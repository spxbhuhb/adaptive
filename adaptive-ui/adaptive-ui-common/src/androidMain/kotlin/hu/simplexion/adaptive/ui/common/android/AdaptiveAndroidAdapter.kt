/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.DisplayMetrics
import android.util.TypedValue.*
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIAdapter
import hu.simplexion.adaptive.ui.common.AdaptiveUIContainerFragment
import hu.simplexion.adaptive.ui.common.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.RenderData
import hu.simplexion.adaptive.ui.common.android.fragment.ViewFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.layout.RawFrame

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
            val frame = RawFrame(0f, 0f, rootContainer.width.toFloat(), rootContainer.height.toFloat())

            it.layoutFrame = frame
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
        val layoutFrame = fragment.layoutFrame

        val point = layoutFrame.point
        val size = layoutFrame.size

        val top = point.top
        val left = point.left
        val width = size.width
        val height = size.height

        fragment.receiver.layoutParams = ViewGroup.LayoutParams(size.width.toInt(), size.height.toInt())
        fragment.receiver.layout(
            left.toInt(),
            top.toInt(),
            (left + width).toInt(),
            (top + height).toInt()
        )
    }

    override fun applyRenderInstructions(fragment: AdaptiveUIFragment<View>) {
        with(fragment) {
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
                        setStroke(it.width.px, it.color.androidColor)
                        borderRadius?.let { radius -> cornerRadius = toPx(radius) }
                    }
                    insets += 0
                }

                backgroundColor?.let {
                    drawables += GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(it.androidColor)
                        borderRadius?.let { radius -> cornerRadius = toPx(radius) }
                    }
                    insets += border?.width.px
                }

                backgroundGradient?.let {
                    drawables += GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT, //
                        intArrayOf(it.start.androidColor, it.end.androidColor),
                    ).apply {
                        borderRadius?.let { radius -> cornerRadius = toPx(radius) }
                    }
                    insets += border?.width.px
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
                        it.left.px,
                        it.top.px,
                        it.right.px,
                        it.bottom.px
                    )
                }

                if (view is TextView) {
                    color?.let { view.setTextColor(it.androidColor) }

                    fontSize?.let { view.textSize = it.value }

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

    override fun toPx(dPixel: DPixel): Float =
        applyDimension(COMPLEX_UNIT_DIP, dPixel.value, displayMetrics)

    val DPixel?.px: Int
        get() = this?.let { applyDimension(COMPLEX_UNIT_DIP, value, displayMetrics).toInt() } ?: 0

    // TODO do we need this conversion? TextView.textSize is in SP
    override fun toPx(sPixel: SPixel): Float =
        applyDimension(COMPLEX_UNIT_SP, sPixel.value, displayMetrics)

    val Color.androidColor
        get() = android.graphics.Color.pack(
            ((value shr 16) and 0xFF).toFloat() / 255f,
            ((value shr 8) and 0xFF).toFloat() / 255f,
            (value and 0xFF).toFloat() / 255f
        ).toColorInt()
}