/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.ui.fragment.AuiImage
import `fun`.adaptive.ui.fragment.AuiText
import `fun`.adaptive.ui.fragment.layout.*
import `fun`.adaptive.ui.fragment.structural.AuiSlot

object AuiFragmentFactory : FoundationFragmentFactory() {
    init {
        add("aui:box") { p,i -> AuiBox(p.adapter as AuiAdapter, p, i) }
        add("aui:column") { p,i -> AuiColumn(p.adapter as AuiAdapter, p, i) }
        add("aui:flowbox") { p, i -> AuiFlowBox(p.adapter as AuiAdapter, p, i) }
        add("aui:grid") { p,i -> AuiGrid(p.adapter as AuiAdapter, p, i) }
        add("aui:image") { p,i -> AuiImage(p.adapter as AuiAdapter, p, i) }
        add("aui:row") { p,i -> AuiRow(p.adapter as AuiAdapter, p, i) }
        add("aui:slot") { p, i -> AuiSlot(p.adapter as AuiAdapter, p, i) }
        add("aui:text") { p,i -> AuiText(p.adapter as AuiAdapter, p, i) }
    }
}