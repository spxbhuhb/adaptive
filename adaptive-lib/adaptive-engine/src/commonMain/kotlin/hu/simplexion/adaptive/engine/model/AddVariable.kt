/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.engine.model

import hu.simplexion.adaptive.adat.Adat

@Adat
class AddVariable(
    val name : String,
    val external : Boolean,
    val signature : String,
    val default : ByteArray
) : AdaptiveEngineOperation