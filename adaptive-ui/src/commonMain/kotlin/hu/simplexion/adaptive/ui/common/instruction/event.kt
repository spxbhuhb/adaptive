/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.cleanStateMask
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.render.event

fun onClick(handler: (event: UIEvent) -> Unit) = OnClick(handler)

fun onCursorDown(handler: (event: UIEvent) -> Unit) = OnCursorDown(handler)
fun onCursorMove(handler: (event: UIEvent) -> Unit) = OnCursorMove(handler)
fun onCursorUp(handler: (event: UIEvent) -> Unit) = OnCursorUp(handler)

/**
 * @property   x   The raw [x] coordinate where the event happened, relative to the frame of the
 *                 fragment that the event handler is attached to.
 *
 * @property   y   The raw [y] coordinate where the event happened, relative to the frame of the
 *                 fragment that the event handler is attached to.
 */
class UIEvent(
    val fragment: AbstractCommonFragment<*>,
    val nativeEvent: Any?,
    val x: Double = Double.NaN,
    val y: Double = Double.NaN
) {
    fun patchIfDirty() {
        val closureOwner = fragment.createClosure.owner
        if (closureOwner.dirtyMask != cleanStateMask) {
            closureOwner.patchInternal()
        }
    }
}

@Adat
abstract class UIEventHandler(
    val handler: (event: UIEvent) -> Unit
) : AdaptiveInstruction {
    fun execute(event: UIEvent) {
        handler(event)
        event.patchIfDirty()
    }
}


@Adat
class OnClick(
    handler: (event: UIEvent) -> Unit
) : UIEventHandler(handler) {
    override fun apply(subject: Any) {
        event(subject) { it.onClick = this }
    }
}

@Adat
class OnCursorDown(
    handler: (event: UIEvent) -> Unit
) : UIEventHandler(handler) {
    override fun apply(subject: Any) {
        event(subject) { it.onCursorDown = this }
    }
}

@Adat
class OnCursorMove(
    handler: (event: UIEvent) -> Unit
) : UIEventHandler(handler) {
    override fun apply(subject: Any) {
        event(subject) { it.onCursorMove = this }
    }
}

@Adat
class OnCursorUp(
    handler: (event: UIEvent) -> Unit
) : UIEventHandler(handler) {
    override fun apply(subject: Any) {
        event(subject) { it.onCursorUp = this }
    }
}