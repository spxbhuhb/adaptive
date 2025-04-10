/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.render

import `fun`.adaptive.ui.DensityIndependentAdapter
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.ui.render.model.ContainerRenderData
import `fun`.adaptive.ui.render.model.DecorationRenderData
import `fun`.adaptive.ui.render.model.EventRenderData
import `fun`.adaptive.ui.render.model.GridRenderData
import `fun`.adaptive.ui.render.model.InputRenderData
import `fun`.adaptive.ui.render.model.LayoutRenderData
import `fun`.adaptive.ui.render.model.TextRenderData

internal inline fun layout(subject: Any, setter: (data: LayoutRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.layout ?: LayoutRenderData(subject.adapter).also { subject.layout = it }))
    }
}

internal inline fun decoration(subject: Any, setter: (data : DecorationRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.decoration ?: DecorationRenderData(subject.adapter).also { subject.decoration = it }))
    }
}

internal inline fun container(subject: Any, setter: (data : ContainerRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.container ?: ContainerRenderData(subject.adapter).also { subject.container = it }))
    }
}

internal inline fun text(subject: Any, setter: (data: TextRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.text ?: TextRenderData().also { subject.text = it }))
    }
}

internal inline fun textAndAdapter(subject: Any, setter: (data: TextRenderData, adapter: DensityIndependentAdapter) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.text ?: TextRenderData().also { subject.text = it }), subject.adapter)
    }
}

internal inline fun grid(subject: Any, setter: (data : GridRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.grid ?: GridRenderData(subject.adapter).also { subject.grid = it }))
    }
}

internal inline fun event(subject: Any, setter: (data : EventRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.event ?: EventRenderData(subject.adapter).also { subject.event = it }))
    }
}

internal inline fun input(subject: Any, setter: (data : InputRenderData) -> Unit) {
    if (subject is AuiRenderData) {
        setter((subject.input ?: InputRenderData(subject.adapter).also { subject.input = it }))
    }
}