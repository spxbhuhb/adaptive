/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.instruction.AlignContent
import hu.simplexion.adaptive.ui.common.instruction.AlignItems
import hu.simplexion.adaptive.ui.common.instruction.JustifyContent
import hu.simplexion.adaptive.ui.common.instruction.JustifyItems
import org.w3c.dom.HTMLElement


fun AbstractCommonFragment<HTMLElement>.align() {
    val style = receiver.style

    when (renderData.container?.alignItems) {
        null -> Unit
        AlignItems.Center -> style.alignItems = "center"
        AlignItems.End -> style.alignItems = "end"
        AlignItems.Start -> style.alignItems = "start"
    }

    when (renderData.container?.justifyItems) {
        null -> Unit
        JustifyItems.Center -> style.setProperty("justify-items", "center")
        JustifyItems.End -> style.setProperty("justify-items", "end")
        JustifyItems.Start -> style.setProperty("justify-items", "start")
    }

    when (renderData.container?.alignContent) {
        null -> Unit
        AlignContent.Center -> style.alignContent = "center"
        AlignContent.End -> style.alignContent = "end"
        AlignContent.Start -> style.alignContent = "start"
    }

    when (renderData.container?.justifyContent) {
        null -> Unit
        JustifyContent.Center -> style.setProperty("justify-content", "center")
        JustifyContent.End -> style.setProperty("justify-content", "end")
        JustifyContent.Start -> style.setProperty("justify-content", "start")
    }
}