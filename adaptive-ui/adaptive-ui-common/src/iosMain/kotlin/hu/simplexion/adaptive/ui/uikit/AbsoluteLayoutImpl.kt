/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.uikit

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UILabel
import platform.UIKit.UIView
import platform.UIKit.UIViewController

@OptIn(BetaInteropApi::class)
@ExportObjCClass
class AbsoluteLayoutImpl(private val controller: UIViewController) {

    private val label: UILabel = UILabel().apply {
        translatesAutoresizingMaskIntoConstraints = false
    }

    init {
        controller.view.addSubview(label)
        setupConstraints()
    }

    private fun setupConstraints() {
        label.centerXAnchor.constraintEqualToAnchor(controller.view.centerXAnchor).active = true
        label.centerYAnchor.constraintEqualToAnchor(controller.view.centerYAnchor).active = true
    }

    fun setText(text: String) {
        label.text = text
    }

}