/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.fragment.*
import `fun`.adaptive.ui.fragment.layout.*

object AuiFragmentFactory : FoundationFragmentFactory() {
    init {
        add("aui:box") { p, i, s -> AuiBox(p.adapter as AuiAdapter, p, i) }
        add("aui:flowbox") { p, i, s -> AuiFlowBox(p.adapter as AuiAdapter, p, i) }
        add("aui:column") { p, i, s -> AuiColumn(p.adapter as AuiAdapter, p, i) }
        add("aui:grid") { p, i, s -> AuiGrid(p.adapter as AuiAdapter, p, i) }
        add("aui:image") { p, i, s -> AuiImage(p.adapter as AuiAdapter, p, i) }
        add("aui:input") { p, i, s -> AuiInput(p.adapter as AuiAdapter, p, i) }
        add("aui:boundinput") { p, i, s -> AuiBoundInput(p.adapter as AuiAdapter, p, i) }
        add("aui:row") { p, i, s -> AuiRow(p.adapter as AuiAdapter, p, i) }
        add("aui:text") { p, i, s -> AuiText(p.adapter as AuiAdapter, p, i) }
        add("aui:flowtext") { p, i, s -> AuiFlowText(p.adapter as AuiAdapter, p, i) }
        add("aui:rootbox") { p, i, s -> AuiRootBox(p.adapter as AuiAdapter, p, i) }
        add("aui:draggable") { p, i, s -> AuiDraggable(p.adapter as AuiAdapter, p, i) }
        add("aui:droptarget") { p, i, s -> AuiDropTarget(p.adapter as AuiAdapter, p, i) }
        add("aui:rectangle") { p, i, s -> AuiRectangle(p.adapter as AuiAdapter, p, i) }
    }
}