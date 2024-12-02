package `fun`.adaptive.kotlin.foundation.ir.util

import `fun`.adaptive.kotlin.foundation.ir.arm2ir.ClassBoundIrBuilder
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.util.companionObject

fun ClassBoundIrBuilder.adatCompanionOrNull(type : IrType) : IrExpression =
    if (type.isSubtypeOfClass(pluginContext.adatClass)) {
        val owner = type.classOrFail.owner
        if (owner.modality == Modality.ABSTRACT) {
            irNull()
        } else {
            val companionClass = checkNotNull(owner.companionObject()) { "missing Adat companion; ${type.classFqName}" }
            irGetObject(companionClass.symbol)
        }
    } else {
        irNull()
    }