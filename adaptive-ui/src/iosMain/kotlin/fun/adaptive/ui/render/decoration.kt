/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.platform.uiColor
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.render.model.DecorationRenderData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreFoundation.CFRetain
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.Foundation.CFBridgingRelease
import platform.QuartzCore.CAGradientLayer
import platform.QuartzCore.CATransaction
import platform.UIKit.UIColor
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun applyDecoration(fragment: AbstractAuiFragment<UIView>) {
    val previousData = fragment.previousRenderData
    val currentData = fragment.renderData

    val previous = previousData.decoration
    val current = currentData.decoration

    val view = fragment.receiver

    // ----  background color  ----

    view.backgroundColor = current?.backgroundColor?.uiColor ?: UIColor.clearColor()

    // ----  border  ----

    val borderWidth = current?.border?.top // FIXME individual border widths
    val borderColor = current?.border?.color
    val cornerRadius = current?.cornerRadius

    if (borderWidth != current?.border?.top) {
        view.layer.borderWidth = borderWidth ?: 0.0
    }

    if (borderColor != current?.border?.color) {
        view.layer.borderColor = borderColor?.uiColor?.CGColor
    }

    // ----  corner radius  ----

    if (cornerRadius != current?.cornerRadius) {
        view.layer.cornerRadius = cornerRadius?.topLeft ?: 0.0 // FIXME individual radii for corners
    }

    // ----  background gradient  -----

    applyGradient(view, currentData, current, previous)
}

@OptIn(ExperimentalForeignApi::class)
private fun applyGradient(
    view: UIView,
    data: AuiRenderData,
    current: DecorationRenderData?,
    previous: DecorationRenderData?
) {

    val gradient = current?.backgroundGradient

    if (gradient == null && previous?.backgroundGradient == null) return

    CATransaction.begin()
    CATransaction.setDisableActions(true)

    (view.layer.sublayers?.firstOrNull { it is CAGradientLayer } as CAGradientLayer?)
        ?.removeFromSuperlayer()

    if (gradient == null || data.finalHeight == 0.0 || data.finalWidth == 0.0) {
        CATransaction.commit()
        return
    }

    val layer = CAGradientLayer()

    layer.frame = CGRectMake(0.0, 0.0, data.finalWidth, data.finalHeight)

    layer.colors = listOf(
        CFBridgingRelease(CFRetain(gradient.start.uiColor.CGColor)),
        CFBridgingRelease(CFRetain(gradient.end.uiColor.CGColor))
    )

    layer.startPoint = gradient.startPosition.let { p -> CGPointMake(p.left.value, p.top.value) }
    layer.endPoint = gradient.endPosition.let { p -> CGPointMake(p.left.value, p.top.value) }

    layer.cornerRadius = current.cornerRadius?.topLeft ?: 0.0 // FIXME individual radii for corners

    view.layer.insertSublayer(layer, 0u)

    CATransaction.commit()
}