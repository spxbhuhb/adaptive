package `fun`.adaptive.kotlin.foundation.ir.util

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.companionObject

fun ClassBoundIrBuilder.adatCompanionOrNull(type : IrType) : IrExpression =
    if (type.isSubtypeOfClass(pluginContext.adatClass)) {
        val companionClass = checkNotNull(type.classOrFail.owner.companionObject()) { "missing Adat companion; ${type.classFqName}" }
        irGetObject(companionClass.symbol)
    } else {
        irNull()
    }