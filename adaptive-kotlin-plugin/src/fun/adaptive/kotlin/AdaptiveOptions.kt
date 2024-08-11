/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin

import java.io.File

class AdaptiveOptions(
    val pluginDebug: Boolean,
    val pluginLogDir: File?,
    val dumpKotlinLike: Boolean,
    val dumpIR: Boolean
)