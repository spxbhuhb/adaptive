/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation.instruction

import hu.simplexion.adaptive.foundation.AdaptiveFragment

/**
 * Instructions that detach fragments have to implement this interface.
 */
interface DetachHandler {
    fun detach(origin : AdaptiveFragment, detachIndex : Int)
}