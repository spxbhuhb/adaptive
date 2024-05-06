/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.wireformat

import org.jetbrains.kotlin.ir.backend.js.utils.asString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.argumentsCount
import org.jetbrains.kotlin.types.checker.SimpleClassicTypeSystemContext.getArguments

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

        "kotlin.String" to "S",

        "kotlin.UInt" to "+I",
        "kotlin.UShort" to "+S",
        "kotlin.UByte" to "+B",
        "kotlin.ULong" to "+J",

        "kotlin.UIntArray" to "[+I",
        "kotlin.UShortArray" to "[+S",
        "kotlin.UByteArray" to "[+B",
        "kotlin.ULongArray" to "[+J",

        "hu.simplexion.adaptive.utility.UUID" to "U"
    )

    val reversedShorthands = shorthands.entries.associate { it.value to it.key }

    fun functionSignature(function: IrFunction): String {
        val parts = mutableListOf<String>()

        function.valueParameters.mapTo(parts) {
            typeSignature(it.type)
        }

        return function.name.identifier + "(" + parts.joinToString("") + ")" + typeSignature(function.returnType)
    }

    fun typeSignature(irType: IrType): String {
        val typeFqName = checkNotNull(irType.classFqName) { "type without null classFqName: ${irType.asString()}" }
        val typeName = typeFqName.asString()

        val builtin = shorthands[typeFqName.asString()]
        if (builtin != null) return builtin.addNull(irType)

        if (irType.argumentsCount() == 0) {
            return "L$typeName;".addNull(irType)
        }

        val argumentSignatures =
            irType.getArguments().joinToString(";") {
                check(it is IrType) { "type argument $it is not an IrType type: ${irType.asString()}" }
                typeSignature(it as IrType)
            }

        return "L$typeFqName<$argumentSignatures>;".addNull(irType)
    }

    fun String.addNull(irType: IrType) =
        if (irType.isNullable()) "$this?" else this

}