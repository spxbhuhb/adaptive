/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.view.View.*
import android.widget.TextView
import hu.simplexion.adaptive.ui.common.adapter.AdaptiveUIFragment
import hu.simplexion.adaptive.ui.common.instruction.AdaptiveUIEvent
import hu.simplexion.adaptive.ui.common.instruction.RenderInstructions
import hu.simplexion.adaptive.ui.common.instruction.TextAlign

fun AdaptiveUIFragment.applyRenderInstructions() {
    renderInstructions = RenderInstructions(instructions)
    // FIXME should clear actual UI settings when null

    tracePatterns = renderInstructions.tracePatterns

    val view = (receiver as View)

    with(renderInstructions) {

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
                onClick !!.handler(AdaptiveUIEvent(this@applyRenderInstructions, it))
            }
        }
    }

}