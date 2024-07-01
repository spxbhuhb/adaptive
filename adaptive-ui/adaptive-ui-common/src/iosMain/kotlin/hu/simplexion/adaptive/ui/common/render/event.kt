/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.platform.GestureTarget
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UITapGestureRecognizer
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
fun applyEvents(fragment: AbstractCommonFragment<UIView>) {
    val view = fragment.receiver

    val events = fragment.renderData.event

    val onClick = events?.onClick

    if (onClick == fragment.previousRenderData.event?.onClick) {
        return
    }

    val onClickListener = events?.onClickListener

    if (onClickListener != null) {
        onClickListener as UITapGestureRecognizer
        view.removeGestureRecognizer(onClickListener)
        view.setUserInteractionEnabled(false)
    }

    if (onClick != null) {
        println("$fragment adding gesture recognizer")
        val tap = UITapGestureRecognizer(GestureTarget(fragment, onClick), NSSelectorFromString("viewTapped"))
        //tap.numberOfTapsRequired = 1u
        //tap.delegate = Delegate()
        events.onClickListener = tap
        view.setUserInteractionEnabled(true)
        view.addGestureRecognizer(tap)
    }
}