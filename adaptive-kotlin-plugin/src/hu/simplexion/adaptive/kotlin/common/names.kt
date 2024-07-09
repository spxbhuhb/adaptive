/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.common

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.UnsignedArrayType
import org.jetbrains.kotlin.builtins.UnsignedType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

// ------------------------------------------------------------------------------------
// Kotlin
// ------------------------------------------------------------------------------------

const val KOTLIN = "kotlin"
const val KOTLIN_COLLECTIONS = "kotlin.collections"

object KotlinNames : NamesBase("") {
    val COMPANION_OBJECT = "Companion".name()
}

object KotlinClassIds : NamesBase("kotlin") {
    val ARRAY = "Array".classId { kotlin }
    val LIST = "List".classId { kotlinCollections }
    val MAP = "Map".classId { kotlinCollections }
    val SET = "Set".classId { kotlinCollections }
    val MUTABLE_LIST = "MutableList".classId { kotlinCollections }
    val MUTABLE_MAP = "MutableMap".classId { kotlinCollections }
    val MUTABLE_SET = "MutableSet".classId { kotlinCollections }
}

object KotlinCallableIds : NamesBase("kotlin") {
    val EMPTY_ARRAY = "emptyArray".callableId { kotlin }
    val EMPTY_LIST = "emptyList".callableId { kotlinCollections }
    val EMPTY_MAP = "emptyMap".callableId { kotlinCollections }
    val EMPTY_SET = "emptySet".callableId { kotlinCollections }
    val MUTABLE_LIST_OF = "mutableListOf".callableId { kotlinCollections }
    val MUTABLE_MAP_OF = "mutableMapOf".callableId { kotlinCollections }
    val MUTABLE_SET_OF = "mutableSetOf".callableId { kotlinCollections }
}

class KotlinSymbols(
    override val irContext: IrPluginContext
) : Symbols {

    val emptyArray = KotlinCallableIds.EMPTY_ARRAY.noArg
    val emptyList = KotlinCallableIds.EMPTY_LIST.noArg
    val emptyMap = KotlinCallableIds.EMPTY_MAP.noArg
    val emptySet = KotlinCallableIds.EMPTY_SET.noArg
    val mutableListOf = KotlinCallableIds.MUTABLE_LIST_OF.noArg
    val mutableMapOf = KotlinCallableIds.MUTABLE_MAP_OF.noArg
    val mutableSetOf = KotlinCallableIds.MUTABLE_SET_OF.noArg

    val array = KotlinClassIds.ARRAY.classSymbol
    val list = KotlinClassIds.LIST.classSymbol
    val map = KotlinClassIds.MAP.classSymbol
    val set = KotlinClassIds.SET.classSymbol
    val mutableList = KotlinClassIds.MUTABLE_LIST.classSymbol
    val mutableMap = KotlinClassIds.MUTABLE_MAP.classSymbol
    val mutableSet = KotlinClassIds.MUTABLE_SET.classSymbol
}

class KotlinUnsignedSymbols(
    override val irContext: IrPluginContext,
) : Symbols {

    val uInt = UnsignedType.UINT.classId.classSymbol
    val uShort = UnsignedType.USHORT.classId.classSymbol
    val uByte = UnsignedType.UBYTE.classId.classSymbol
    val uLong = UnsignedType.ULONG.classId.classSymbol

    val uIntArray = UnsignedArrayType.UINTARRAY.classId.classSymbol
    val uShortArray = UnsignedArrayType.USHORTARRAY.classId.classSymbol
    val uByteArray = UnsignedArrayType.UBYTEARRAY.classId.classSymbol
    val uLongArray = UnsignedArrayType.ULONGARRAY.classId.classSymbol

}


// ------------------------------------------------------------------------------------
// Time and date time
// ------------------------------------------------------------------------------------

const val TIME_PACKAGE = "kotlin.time"
const val DATETIME_PACKAGE = "kotlinx.datetime"

object DateTimeClassIds : NamesBase("") {
    val time = FqName(TIME_PACKAGE)
    val datetime = FqName(DATETIME_PACKAGE)

    val duration = "Duration".classId { time }
    val instant = "Instant".classId { datetime }
    val localDateTime = "LocalDateTime".classId { datetime }
    val localDate = "LocalDate".classId { datetime }
    val localTime = "LocalTime".classId { datetime }
}

class DateTimeTypes(
    override val irContext: IrPluginContext
) : Symbols {

    val duration = DateTimeClassIds.duration.classSymbol
    val instant = DateTimeClassIds.instant.classSymbol
    val localDateTime = DateTimeClassIds.localDateTime.classSymbol
    val localDate = DateTimeClassIds.localDate.classSymbol
    val localTime = DateTimeClassIds.localTime.classSymbol

}

// ------------------------------------------------------------------------------------
// Adaptive
// ------------------------------------------------------------------------------------

const val UTILITY_PACKAGE = "hu.simplexion.adaptive.utility"

object AdaptiveFqNames : NamesBase("") {
    val UUID = FqName("$UTILITY_PACKAGE.UUID")
}

object AdaptiveClassIds : NamesBase("") {
    val utility = FqName(UTILITY_PACKAGE)

    val UUID = "UUID".classId { utility }
}

object AdaptiveCallableIds : NamesBase(UTILITY_PACKAGE) {
    val instant = "instant".callableId()
    val zeroDuration = "zeroDuration".callableId()
    val localDateTime = "localDateTime".callableId()
    val localDate = "localDate".callableId()
    val localTime = "localTime".callableId()
}

class AdaptiveSymbols(
    override val irContext: IrPluginContext
) : Symbols {

    val uuid = AdaptiveClassIds.UUID.classSymbol

    val instant = AdaptiveCallableIds.instant.functionSymbol
    val zeroDuration = AdaptiveCallableIds.zeroDuration.functionSymbol
    val localDateTime = AdaptiveCallableIds.localDateTime.functionSymbol
    val localDate = AdaptiveCallableIds.localDate.functionSymbol
    val localTime = AdaptiveCallableIds.localTime.functionSymbol
}

// ------------------------------------------------------------------------------------
// Base classes
// ------------------------------------------------------------------------------------

open class NamesBase(
    defaultPackage: String
) {
    val defaultPackage = FqName(defaultPackage)
    val kotlin = FqName(KOTLIN)
    val kotlinCollections = FqName(KOTLIN_COLLECTIONS)

    protected fun String.name() = Name.identifier(this)

    protected fun String.fqName() = FqName(this)

    protected fun String.fqName(packageFun: () -> String) = FqName(packageFun() + "." + this)

    protected fun String.classId(packageFun: () -> FqName = { defaultPackage }) =
        ClassId(packageFun(), Name.identifier(this))

    protected fun String.callableId(packageFun: () -> FqName = { defaultPackage }) =
        CallableId(packageFun(), Name.identifier(this))

}

interface Symbols {
    val irContext: IrPluginContext

    val CallableId.noArg
        get() = irContext.referenceFunctions(this).first { it.owner.valueParameters.isEmpty() }

    val ClassId.classSymbol
        get() = checkNotNull(irContext.referenceClass(this)) { "Missing class ${this.asString()}" }

    val CallableId.functionSymbol
        get() = irContext.referenceFunctions(this).single()
}