/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.uikit.adapter

import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake

@OptIn(ExperimentalForeignApi::class)
fun BoundingRect.toCGRect() =
    CGRectMake(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
