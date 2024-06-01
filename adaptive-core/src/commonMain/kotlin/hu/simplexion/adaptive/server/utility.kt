/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.server

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.server.builtin.ServerFragmentImpl
import hu.simplexion.adaptive.utility.firstOrNullIfInstance

// TODO move server utilities to the general query package
inline fun <reified T : AdaptiveInstruction> ServerFragmentImpl.has() : Boolean =
    fragment?.instructions?.any { it is T } ?: false

inline fun <reified T : AdaptiveInstruction> ServerFragmentImpl.ifHas(crossinline block : (it:T) -> Unit) =
    fragment?.instructions?.firstOrNullIfInstance<T>()?.also { block(it) }