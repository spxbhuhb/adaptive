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

    val adatCompanion = ClassIds.ADAT_COMPANION.classSymbol()
    val adatClassWireFormat = ClassIds.ADAT_CLASS_WIREFORMAT.classSymbol()

    val wireFormatRegistry = ClassIds.WIREFORMAT_REGISTRY.classSymbol()
    val wireFormatRegistrySet = checkNotNull(wireFormatRegistry.getSimpleFunction("set"))

    val arrayGet = checkNotNull(irContext.irBuiltIns.arrayClass.getSimpleFunction("get"))

    val uIntType = irContext.referenceClass(UnsignedType.UINT.classId) !!.defaultType
    val uByteType = irContext.referenceClass(UnsignedType.UBYTE.classId) !!.defaultType
    val uShortType = irContext.referenceClass(UnsignedType.USHORT.classId) !!.defaultType
    val uLongType = irContext.referenceClass(UnsignedType.ULONG.classId) !!.defaultType

    val propertiesFun = CallableIds.properties.functions().first()
    val adatCompanionOfFun = CallableIds.adatCompanionOf.functions().first()

}