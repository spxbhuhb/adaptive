/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.adat.ir

import `fun`.adaptive.kotlin.AdaptiveOptions
import `fun`.adaptive.kotlin.adat.CallableIds
import `fun`.adaptive.kotlin.adat.ClassIds
import `fun`.adaptive.kotlin.adat.ir.immutable.ImmutableCache
import `fun`.adaptive.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.UnsignedArrayType
import org.jetbrains.kotlin.builtins.UnsignedType
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrFunctionSymbol
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class AdatPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val sensibleCache = mutableMapOf<String, IrExpression?>()

    val shortImmutableCache = ImmutableCache(true)
    val qualifiedImmutableCache = ImmutableCache(false)

    val adatClass = ClassIds.ADAT_CLASS.classSymbol()
    val adatCompanion = ClassIds.ADAT_COMPANION.classSymbol()
    val adatClassWireFormat = ClassIds.ADAT_CLASS_WIREFORMAT.classSymbol()

    val wireFormatRegistry = ClassIds.WIREFORMAT_REGISTRY.classSymbol()
    val wireFormatRegistrySet = checkNotNull(wireFormatRegistry.getSimpleFunction("set"))

    val exposedResultRow = ClassIds.RESULT_ROW.symbolOrNull()
    val exposedResultRowGet = exposedResultRow?.getSimpleFunction("get")

    val exposedColumn = ClassIds.COLUMN.symbolOrNull()

    val commonUuid = ClassIds.COMMON_UUID.classSymbol()

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val entityId = ClassIds.ENTITY_ID.symbolOrNull()
    val javaUuid = ClassIds.JAVA_UUID.symbolOrNull()
    val javaUuidType = javaUuid?.defaultType
    val javaUuidTypeN = javaUuidType?.makeNullable()

    val asCommon = CallableIds.asCommon.functions()

    fun IrFunctionSymbol.isExtensionOf(symbol: IrClassSymbol?, nullable: Boolean) =
        symbol != null && owner.extensionReceiverParameter?.type?.let { it.isSubtypeOfClass(symbol) && it.isNullable() == nullable } == true

    val asCommonEntityId = asCommon.firstOrNull { it.isExtensionOf(entityId, false) }
    val asCommonEntityIdN = asCommon.firstOrNull { it.isExtensionOf(entityId, true) }

    val asCommonUuid = asCommon.firstOrNull { it.isExtensionOf(javaUuid, false) }
    val asCommonUuidN = asCommon.firstOrNull { it.isExtensionOf(javaUuid, true) }

    val asJavaUuid = CallableIds.asJava.functions().firstOrNull { it.isExtensionOf(commonUuid, false) }
    val asJavaUuidN = CallableIds.asJava.functions().firstOrNull { it.isExtensionOf(commonUuid, true) }

    val asEntityId = CallableIds.asEntityID.functions().firstOrNull {
        it.isExtensionOf(commonUuid, false)
            && it.owner.valueParameters.firstOrNull()?.type?.isSubtypeOfClass(exposedColumn !!) == true
    }

    val asEntityIdN = CallableIds.asEntityID.functions().firstOrNull {
        it.isExtensionOf(commonUuid, true)
            && it.owner.valueParameters.firstOrNull()?.type?.isSubtypeOfClass(exposedColumn !!) == true
    }

    val uIntType = irContext.referenceClass(UnsignedType.UINT.classId) !!.defaultType
    val uByteType = irContext.referenceClass(UnsignedType.UBYTE.classId) !!.defaultType
    val uShortType = irContext.referenceClass(UnsignedType.USHORT.classId) !!.defaultType
    val uLongType = irContext.referenceClass(UnsignedType.ULONG.classId) !!.defaultType

    val uIntArrayType = irContext.referenceClass(UnsignedArrayType.UINTARRAY.classId) !!.defaultType
    val uByteArrayType = irContext.referenceClass(UnsignedArrayType.UBYTEARRAY.classId) !!.defaultType
    val uShortArrayType = irContext.referenceClass(UnsignedArrayType.USHORTARRAY.classId) !!.defaultType
    val uLongArrayType = irContext.referenceClass(UnsignedArrayType.ULONGARRAY.classId) !!.defaultType

}