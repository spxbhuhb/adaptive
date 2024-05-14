/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.android.adapter

import android.content.Context
import android.view.ViewGroup
import hu.simplexion.adaptive.base.Adaptive
import hu.simplexion.adaptive.base.AdaptiveAdapter
import hu.simplexion.adaptive.base.AdaptiveEntry

/**
 * The entry point of an Adaptive Android component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun android(
    context : Context,
    rootView : ViewGroup,
    trace : Boolean = false,
    @Adaptive block: (adapter : AdaptiveAdapter<*>) -> Unit
) : AdaptiveViewAdapter =
    AdaptiveViewAdapter(
        context, rootView, trace
    ).also {
        block(it)
        it.mounted()
    }