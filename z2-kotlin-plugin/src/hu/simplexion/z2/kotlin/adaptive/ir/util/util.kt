/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.util

import hu.simplexion.z2.kotlin.adaptive.Strings
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.parentOrNull
import java.util.*

fun IrFunction.adaptiveClassFqName(): FqName {
    val parent = kotlinFqName.parentOrNull() ?: FqName.ROOT

    return if (isAnonymousFunction || name.asString() == "<anonymous>") {
        val postfix = this.file.name.replace(".kt", "").capitalizeFirstChar() + startOffset.toString()
        parent.child(Name.identifier(Strings.ADAPTIVE_ROOT + postfix))
    } else {
        parent.child(Name.identifier("Adaptive" + name.identifier.capitalizeFirstChar()))
    }
}

fun String.capitalizeFirstChar() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
