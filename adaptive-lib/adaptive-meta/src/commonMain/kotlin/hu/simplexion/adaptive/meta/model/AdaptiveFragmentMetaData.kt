/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.meta.model

class AdaptiveFragmentMetaData(
    val impl: String,
    val patchInstructions: List<AdaptivePatchInstruction<*>>
)

