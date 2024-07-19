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

fun onMove(handler: (event: UIEvent) -> Unit) = OnMove(handler)

fun onPrimaryDown(handler: (event: UIEvent) -> Unit) = OnPrimaryDown(handler)
fun onPrimaryUp(handler: (event: UIEvent) -> Unit) = OnPrimaryUp(handler)

fun onSecondaryDown(handler: (event: UIEvent) -> Unit) = OnSecondaryDown(handler)
fun onSecondaryUp(handler: (event: UIEvent) -> Unit) = OnSecondaryUp(handler)

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
    val position: Position =
        Position(
            fragment.uiAdapter.toDp(y),
            fragment.uiAdapter.toDp(x)
        )

    fun patchIfDirty() {
        val closureOwner = fragment.createClosure.owner
        if (closureOwner.dirtyMask != cleanStateMask) {
            closureOwner.patchInternal()
        }
    }
}

interface UIEventHandler : AdaptiveInstruction {

    val handler: (event: UIEvent) -> Unit

    fun execute(event: UIEvent) {
        handler(event)
        event.patchIfDirty()
    }
}


@Adat
class OnClick(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) { it.onClick = this }
    }
}

@Adat
class OnMove(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onMove = this
        }
    }
}

@Adat
class OnPrimaryDown(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onPrimaryDown = this
        }
    }
}


@Adat
class OnPrimaryUp(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onPrimaryUp = this
        }
    }
}

@Adat
class OnSecondaryDown(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onSecondaryDown = this
        }
    }
}


@Adat
class OnSecondaryUp(
    override val handler: (event: UIEvent) -> Unit
) : UIEventHandler {
    override fun apply(subject: Any) {
        event(subject) {
            it.additionalEvents = true
            it.onSecondaryUp = this
        }
    }
}