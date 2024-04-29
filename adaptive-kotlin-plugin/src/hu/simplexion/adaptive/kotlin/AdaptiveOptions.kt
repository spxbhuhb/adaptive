/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin

import java.io.File

class AdaptiveOptions(
    val resourceOutputDir : File?,
    val pluginDebug : Boolean,
    val pluginLogDir : File?,
    val dumpKotlinLike : Boolean
)