/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.engine.model

import hu.simplexion.adaptive.adat.Adat

@Adat
class AddBinding(
    val impl : String,
    val sourceFragment : Int,
    val sourceProperty : String,
    val targetFragment : Int,
    val targetProperty : String,
    val extensions : String
) : AdaptiveEngineOperation