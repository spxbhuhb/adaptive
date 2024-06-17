/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

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
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.ContainerViewGroup
import hu.simplexion.adaptive.ui.common.platform.MediaMetrics
import hu.simplexion.adaptive.ui.common.platform.StructuralViewGroup
import hu.simplexion.adaptive.ui.common.render.*
import hu.simplexion.adaptive.ui.common.support.AbstractContainerFragment
import hu.simplexion.adaptive.ui.common.support.RawFrame

open class CommonAdapter(
    val context: Context,
    final override val rootContainer: ViewGroup
) : AbstractCommonAdapter<View, ContainerViewGroup>() {

    override val fragmentFactory = CommonFragmentFactory

    val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    override fun makeContainerReceiver(fragment: AbstractContainerFragment<View, ContainerViewGroup>): ContainerViewGroup =
        ContainerViewGroup(context, fragment)

    override fun makeStructuralReceiver(fragment: AbstractContainerFragment<View, ContainerViewGroup>): ContainerViewGroup =
        StructuralViewGroup(context, fragment)

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainerFragment<View, ContainerViewGroup>> {
            val frame = RawFrame(0.0, 0.0, rootContainer.width.toDouble(), rootContainer.height.toDouble())

            it.layoutFrame = frame
            it.measure()
            it.layout(frame)

            it.receiver.layoutParams = LinearLayout.LayoutParams(rootContainer.width, rootContainer.height)
            rootContainer.addView(it.receiver)
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainerFragment<View, ContainerViewGroup>> {
            rootContainer.removeView(it.receiver)
        }
    }

    override fun addActual(containerReceiver: ContainerViewGroup, itemReceiver: View) {
        containerReceiver.addView(itemReceiver)
    }

    override fun removeActual(itemReceiver: View) {
        (itemReceiver.parent as ContainerViewGroup).removeView(itemReceiver)
    }

    override fun applyLayoutToActual(fragment: AbstractCommonFragment<View>) {
        applyLayoutToActual(fragment.layoutFrame, fragment.receiver)
    }

    fun applyLayoutToActual(frame: RawFrame, receiver: View) {
        val top = frame.top
        val left = frame.left
        val width = frame.width
        val height = frame.height

        receiver.layoutParams = ViewGroup.LayoutParams(width.toInt(), height.toInt())

        receiver.layout(
            left.toInt(),
            top.toInt(),
            (left + width).toInt(),
            (top + height).toInt()
        )
    }

    override fun applyRenderInstructions(fragment: AbstractCommonFragment<View>) {

        val renderData = CommonRenderData(this, fragment.instructions)
        val view = fragment.receiver

        // FIXME should clear actual UI settings when null

        if (renderData.tracePatterns.isNotEmpty()) {
            fragment.tracePatterns = renderData.tracePatterns
        }

        renderData.layout { it.apply(view) }
        renderData.decoration { it.apply(view, renderData.layout) }
        renderData.text { it.apply(view) }
        renderData.event { it.apply(view, fragment) }
    }

    fun LayoutRenderData.apply(view: View) {
        padding {
            view.setPadding(it.left.px, it.top.px, it.right.px, it.bottom.px)
        }
    }

    fun DecorationRenderData.apply(view: View, layout: LayoutRenderData?) {
        val drawables = mutableListOf<Drawable>()
        val insets = mutableListOf<Int>()

        val borderWidth = layout?.border?.top // FIXME individual border widths for android
        val borderColor = this.borderColor

        if (borderWidth != null && borderColor != null) {
            drawables += GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(android.graphics.Color.TRANSPARENT)
                setStroke(borderWidth.toInt(), borderColor.androidColor)
                borderRadius { cornerRadii = it.toFloatArray() }
            }
            insets += 0
        }

        backgroundColor {
            drawables += GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(it.androidColor)
                borderRadius { cornerRadii = it.toFloatArray() }
            }
            borderWidth { b -> insets += b.px }
        }

        backgroundGradient?.let {
            drawables += GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, //
                intArrayOf(it.start.androidColor, it.end.androidColor),

                ).apply {
                borderRadius { b -> cornerRadii = b.toFloatArray() }
            }
            borderWidth { b -> insets += b.px }
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
    }

    fun TextRenderData.apply(view: View) {
        if (view !is TextView) return

        color { view.setTextColor(it.androidColor) }
        fontSize { view.textSize = it.value.toFloat() }
        fontWeight {
            // FIXME proper typeface mapping
            if (it > 500) view.setTypeface(null, Typeface.BOLD)
        }
        letterSpacing { view.letterSpacing = it.toFloat() }

        when (align) {
            TextAlign.Start -> view.textAlignment = TEXT_ALIGNMENT_VIEW_START
            TextAlign.Center -> view.textAlignment = TEXT_ALIGNMENT_CENTER
            TextAlign.End -> view.textAlignment = TEXT_ALIGNMENT_VIEW_END
            null -> view.textAlignment = TEXT_ALIGNMENT_CENTER
        }
    }

    fun EventRenderData.apply(view: View, fragment: AbstractCommonFragment<View>) {
        onClickListener {
            // TODO How to remove an event listener on android?
        }

        onClick { oc ->
            view.setOnClickListener {
                oc.execute(AdaptiveUIEvent(fragment, it))
            }
        }
    }

    inline operator fun <reified T : Any> T?.invoke(function: (it: T) -> Unit) {
        if (this != null) {
            function(this)
        }
    }

    override fun toPx(dPixel: DPixel): Double =
        applyDimension(COMPLEX_UNIT_DIP, dPixel.value.toFloat(), displayMetrics).toDouble()

    val Double.px: Int
        inline get() = toInt()

    val DPixel?.px: Int
        get() = this?.let { applyDimension(COMPLEX_UNIT_DIP, value.toFloat(), displayMetrics).toInt() } ?: 0

    val DPixel?.floatToZero: Float
        get() = this?.value?.toFloat() ?: 0f

    // TODO do we need this conversion? TextView.textSize is in SP
    override fun toPx(sPixel: SPixel): Double =
        applyDimension(COMPLEX_UNIT_SP, sPixel.value.toFloat(), displayMetrics).toDouble()

    fun BorderRadius.toFloatArray(): FloatArray {
        return floatArrayOf(
            applyDimension(COMPLEX_UNIT_SP, topLeft.floatToZero, displayMetrics),
            applyDimension(COMPLEX_UNIT_SP, topRight.floatToZero, displayMetrics),
            applyDimension(COMPLEX_UNIT_SP, bottomRight.floatToZero, displayMetrics),
            applyDimension(COMPLEX_UNIT_SP, bottomLeft.floatToZero, displayMetrics)
        )
    }

    val Color.androidColor
        get() = android.graphics.Color.pack(
            ((value shr 16) and 0xFFu).toFloat() / 255f,
            ((value shr 8) and 0xFFu).toFloat() / 255f,
            (value and 0xFFu).toFloat() / 255f
        ).toColorInt()

    override var mediaMetrics = MediaMetrics(rootContainer.width.toDouble(), rootContainer.height.toDouble())
}