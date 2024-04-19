package hu.simplexion.z2.kotlin

import java.io.File

class Z2Options(
    val resourceOutputDir : File?,
    val pluginDebug : Boolean,
    val pluginLogDir : File?,
    val dumpKotlinLike : Boolean
)