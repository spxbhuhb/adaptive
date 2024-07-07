/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.render

import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter

internal inline fun layout(subject: Any, setter: (data: LayoutRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.layout ?: LayoutRenderData(subject.adapter).also { subject.layout = it }))
    }
}

internal inline fun decoration(subject: Any, setter: (data : DecorationRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.decoration ?: DecorationRenderData(subject.adapter).also { subject.decoration = it }))
    }
}

internal inline fun container(subject: Any, setter: (data : ContainerRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.container ?: ContainerRenderData(subject.adapter).also { subject.container = it }))
    }
}

internal inline fun text(subject: Any, setter: (data: TextRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.text ?: TextRenderData().also { subject.text = it }))
    }
}

internal inline fun textAndAdapter(subject: Any, setter: (data: TextRenderData, adapter: AbstractCommonAdapter<*, *>) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.text ?: TextRenderData().also { subject.text = it }), subject.adapter)
    }
}

internal inline fun grid(subject: Any, setter: (data : GridRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.grid ?: GridRenderData(subject.adapter).also { subject.grid = it }))
    }
}

internal inline fun event(subject: Any, setter: (data : EventRenderData) -> Unit) {
    if (subject is CommonRenderData) {
        setter((subject.event ?: EventRenderData(subject.adapter).also { subject.event = it }))
    }
}