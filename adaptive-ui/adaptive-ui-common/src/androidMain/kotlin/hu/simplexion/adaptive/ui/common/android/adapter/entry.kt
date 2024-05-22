/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.android.adapter

import android.content.Context
import android.view.ViewGroup
import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveEntry
import hu.simplexion.adaptive.foundation.AdaptiveFragmentFactory

/**
 * The entry point of an Adaptive Android component tree.
 *
 * **IMPORTANT** variables declared outside the block are **NOT** reactive
 */
@AdaptiveEntry
fun android(
    context : Context,
    rootView : ViewGroup,
    vararg imports : AdaptiveFragmentFactory,
    trace : Boolean = false,
    @Adaptive block: (adapter : AdaptiveAdapter) -> Unit
) : AdaptiveAndroidAdapter =
    AdaptiveAndroidAdapter(
        context, rootView, trace
    ).also {
        it.fragmentFactory += imports
        block(it)
        it.mounted()
    }