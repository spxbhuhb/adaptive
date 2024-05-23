/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.android.adapter

import androidx.core.graphics.toColorInt
import hu.simplexion.adaptive.ui.common.instruction.Color

fun Color.toAndroidColor() =
    android.graphics.Color.pack(
        ((value shr 16) and 0xFF).toFloat() / 255f,
        ((value shr 8) and 0xFF).toFloat() / 255f ,
        (value and 0xFF).toFloat() / 255f
    ).toColorInt()