/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.wireformat

import `fun`.adaptive.kotlin.common.isSubclassOf
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.*
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.isTypeParameter

object Signature {

    val shorthands = mapOf(
        "kotlin.Unit" to "0",

        "kotlin.Boolean" to "Z",
        "kotlin.Int" to "I",
        "kotlin.Short" to "S",
        "kotlin.Byte" to "B",
        "kotlin.Long" to "J",
        "kotlin.Float" to "F",
        "kotlin.Double" to "D",
        "kotlin.Char" to "C",

        "kotlin.BooleanArray" to "[Z",
        "kotlin.IntArray" to "[I",
        "kotlin.ByteArray" to "[B",
        "kotlin.ShortArray" to "[S",
        "kotlin.LongArray" to "[J",
        "kotlin.FloatArray" to "[F",
        "kotlin.DoubleArray" to "[D",
        "kotlin.CharArray" to "[C",

        "kotlin.String" to "T",

        "kotlin.UInt" to "+I",
        "kotlin.UShort" to "+S",
        "kotlin.UByte" to "+B",
        "kotlin.ULong" to "+J",

        "kotlin.UIntArray" to "[+I",
        "kotlin.UShortArray" to "[+S",
        "kotlin.UByteArray" to "[+B",
        "kotlin.ULongArray" to "[+J",

        "fun.adaptive.utility.UUID" to "U",

        "fun.adaptive.wireformat.builtin.PolymorphicWireFormat" to "*"
    )

    val reversedShorthands = shorthands.entries.associate { it.value to it.key }

    fun functionSignature(function: IrFunction, adatClass: IrClassSymbol): String {
        val parts = mutableListOf<String>()

        function.valueParameters.mapTo(parts) {
            typeSignature(it.type, adatClass)
        }

        return function.name.identifier + "(" + parts.joinToString("") + ")" + typeSignature(function.returnType, adatClass)
    }

    fun typeSignature(irType: IrType, adatClass: IrClassSymbol): String {
        if (irType.isTypeParameter()) return "*"

        val typeFqName = checkNotNull(irType.classFqName) { "type without null classFqName: ${irType.dumpKotlinLike()}" }
        val typeName = typeFqName.asString()

        val builtin = shorthands[typeFqName.asString()]
        if (builtin != null) return builtin.addNull(irType)

        check(irType is IrSimpleType)

        val owner = irType.classOrNull?.owner
        if (owner != null && owner.modality != Modality.FINAL && owner.isSubclassOf(adatClass)) return "*"

        if (irType.arguments.isEmpty()) {
            return "L$typeName;".addNull(irType)
        }

        val argumentSignatures = irType.arguments.joinToString(";") { argumentSignature(it, adatClass) }

        return "L$typeFqName<$argumentSignatures>;".addNull(irType)
    }

    fun argumentSignature(irTypeArgument: IrTypeArgument, adatClass: IrClassSymbol): String {
        if (irTypeArgument !is IrType) return "*"

        val classFqName = irTypeArgument.classFqName ?: return "*"

        if (classFqName.asString().startsWith("kotlin.collections")) {
            return typeSignature(irTypeArgument, adatClass)
        }

        val irClass = irTypeArgument.classOrNull ?: return "*"

        val owner = irClass.owner
        if (owner.modality != Modality.FINAL && owner.kind != ClassKind.ENUM_CLASS) return "*"

        return typeSignature(irTypeArgument, adatClass)
    }

    fun String.addNull(irType: IrType) =
        if (irType.isNullable()) "$this?" else this

}