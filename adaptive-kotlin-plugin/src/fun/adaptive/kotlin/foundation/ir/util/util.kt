/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.foundation.ir.util

import `fun`.adaptive.kotlin.foundation.Strings
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.name
import org.jetbrains.kotlin.ir.expressions.IrTypeOperator
import org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.isAnonymousFunction
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.name.CallableId
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

fun CallableId.adaptiveClassFqName(): FqName {
    val parent = asSingleFqName().parentOrNull() ?: FqName.ROOT
    return parent.child(Name.identifier("Adaptive" + this.callableName.identifier.capitalizeFirstChar()))
}

fun IrStatement.removeImplicitCoercion(): IrStatement =
    if (this is IrTypeOperatorCall && this.operator == IrTypeOperator.IMPLICIT_COERCION_TO_UNIT) {
        argument
    } else {
        this
    }

fun String.capitalizeFirstChar() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
