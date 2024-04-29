/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.kotlin

import java.io.File

class Z2Options(
    val resourceOutputDir : File?,
    val pluginDebug : Boolean,
    val pluginLogDir : File?,
    val dumpKotlinLike : Boolean
)