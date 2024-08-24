/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.DisplayMetrics
import android.util.TypedValue.*
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.defaultResourceEnvironment
import `fun`.adaptive.ui.api.BackgroundGradient
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.RawCornerRadius
import `fun`.adaptive.ui.instruction.Color
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.SPixel
import `fun`.adaptive.ui.api.UIEvent
import `fun`.adaptive.ui.platform.ContainerViewGroup
import `fun`.adaptive.ui.platform.media.MediaMetrics
import `fun`.adaptive.ui.platform.StructuralViewGroup
import `fun`.adaptive.ui.render.*
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.render.model.DecorationRenderData
import `fun`.adaptive.ui.render.model.EventRenderData
import `fun`.adaptive.ui.render.model.LayoutRenderData
import `fun`.adaptive.ui.render.model.TextRenderData
import `fun`.adaptive.ui.support.navigation.AbstractNavSupport

open class AuiAdapter(
    val context: Context,
    final override val rootContainer: ViewGroup,
    override val backend: BackendAdapter
) : AbstractAuiAdapter<View, ContainerViewGroup>() {

    override val fragmentFactory = AuiFragmentFactory

    val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    override fun makeContainerReceiver(fragment: AbstractContainer<View, ContainerViewGroup>): ContainerViewGroup =
        ContainerViewGroup(context, fragment)

    override fun makeStructuralReceiver(fragment: AbstractContainer<View, ContainerViewGroup>): ContainerViewGroup =
        StructuralViewGroup(context, fragment)

    override fun addActualRoot(fragment: AdaptiveFragment) {
        traceAddActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainer<View, ContainerViewGroup>> {
            it.computeLayout(rootContainer.width.toDouble(), rootContainer.height.toDouble())
            it.placeLayout(0.0, 0.0)
            it.receiver.layoutParams = LinearLayout.LayoutParams(rootContainer.width, rootContainer.height)
            rootContainer.addView(it.receiver)
        }
    }

    override fun removeActualRoot(fragment: AdaptiveFragment) {
        traceRemoveActual(fragment)

        fragment.ifIsInstanceOrRoot<AbstractContainer<View, ContainerViewGroup>> {
            rootContainer.removeView(it.receiver)
        }
    }

    override fun addActual(containerReceiver: ContainerViewGroup, itemReceiver: View) {
        containerReceiver.addView(itemReceiver)
    }

    override fun removeActual(itemReceiver: View) {
        (itemReceiver.parent as ContainerViewGroup).removeView(itemReceiver)
    }

    override fun applyLayoutToActual(fragment: AbstractAuiFragment<View>) {
        val data = fragment.renderData

        val top = data.finalTop
        val left = data.finalLeft
        val width = data.finalWidth
        val height = data.finalHeight

        val receiver = fragment.receiver

        receiver.layoutParams = ViewGroup.LayoutParams(width.toInt(), height.toInt())

        receiver.layout(
            left.toInt(),
            top.toInt(),
            (left + width).toInt(),
            (top + height).toInt()
        )
    }

    override fun applyRenderInstructions(fragment: AbstractAuiFragment<View>) {

        val renderData = AuiRenderData(this, fragment.instructions, fragment.previousRenderData)
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
            view.setPadding(it.start.px, it.top.px, it.end.px, it.bottom.px)
        }
    }

    fun DecorationRenderData.apply(view: View, layout: LayoutRenderData?) {
        val drawables = mutableListOf<Drawable>()
        val insets = mutableListOf<Int>()

        val borderWidth = layout?.border?.top // FIXME individual border widths for android
        val borderColor = this.border?.color
        val cornerRadius = this.cornerRadius

        if (borderWidth != null && borderColor != null) {
            drawables += GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(android.graphics.Color.TRANSPARENT)
                setStroke(borderWidth.toInt(), borderColor.androidColor)
                cornerRadius { cornerRadii = it.toFloatArray() }
            }
            insets += 0
        }

        backgroundColor {
            drawables += GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(it.androidColor)
                cornerRadius { cornerRadii = it.toFloatArray() }
            }
            borderWidth { b -> insets += b.px }
        }

        backgroundGradient?.let {
            drawables += GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, //
                intArrayOf(BackgroundGradient.start.androidColor, BackgroundGradient.end.androidColor),

                ).apply {
                cornerRadius { b -> cornerRadii = b.toFloatArray() }
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

//        when (align) {
//            TextAlign.Start -> view.textAlignment = TEXT_ALIGNMENT_VIEW_START
//            TextAlign.Center -> view.textAlignment = TEXT_ALIGNMENT_CENTER
//            TextAlign.End -> view.textAlignment = TEXT_ALIGNMENT_VIEW_END
//            null -> view.textAlignment = TEXT_ALIGNMENT_CENTER
//        }
    }

    fun EventRenderData.apply(view: View, fragment: AbstractAuiFragment<View>) {
        onClickListener {
            // TODO How to remove an event listener on android?
        }

        onClick { oc ->
            view.setOnClickListener {
                // FIXME iOS UIEvent position
                oc.execute(UIEvent(fragment, it))
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

    override fun toDp(value: Double): DPixel {
        TODO("Not yet implemented")
    }

    val Double.px: Int
        inline get() = toInt()

    // TODO do we need this conversion? TextView.textSize is in SP
    override fun toPx(sPixel: SPixel): Double =
        applyDimension(COMPLEX_UNIT_SP, sPixel.value.toFloat(), displayMetrics).toDouble()

    fun RawCornerRadius.toFloatArray(): FloatArray {
        val topRight = this.topRight.toFloat()
        val topLeft = this.topLeft.toFloat()
        val bottomRight = this.bottomRight.toFloat()
        val bottomLeft = this.bottomLeft.toFloat()

        return floatArrayOf(
            topLeft, topLeft,
            topRight, topRight,
            bottomRight, bottomRight,
            bottomLeft, bottomLeft
        )
    }

    val Color.androidColor
        get() = android.graphics.Color.pack(
            ((value shr 16) and 0xFFu).toFloat() / 255f,
            ((value shr 8) and 0xFFu).toFloat() / 255f,
            (value and 0xFFu).toFloat() / 255f
        ).toColorInt()

    override var mediaMetrics = MediaMetrics(
        rootContainer.width.toDouble(),
        rootContainer.height.toDouble(),
        defaultResourceEnvironment.theme,
        manualTheme
    )

    override val navSupport: AbstractNavSupport
        get() = TODO("Not yet implemented")
}