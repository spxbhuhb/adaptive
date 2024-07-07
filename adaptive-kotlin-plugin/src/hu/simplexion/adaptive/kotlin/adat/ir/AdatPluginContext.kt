/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.adat.CallableIds
import hu.simplexion.adaptive.kotlin.adat.ClassIds
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isSubtypeOfClass
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.getSimpleFunction

class AdatPluginContext(
    irContext: IrPluginContext,
    options: AdaptiveOptions
) : AbstractPluginContext(irContext, options) {

    val adatClass = ClassIds.ADAT_CLASS.classSymbol()
    val adatCompanion = ClassIds.ADAT_COMPANION.classSymbol()
    val adatClassWireFormat = ClassIds.ADAT_CLASS_WIREFORMAT.classSymbol()

    val wireFormatRegistry = ClassIds.WIREFORMAT_REGISTRY.classSymbol()
    val wireFormatRegistrySet = checkNotNull(wireFormatRegistry.getSimpleFunction("set"))

    val exposedResultRow = ClassIds.RESULT_ROW.symbolOrNull()
    val exposedResultRowGet = exposedResultRow?.getSimpleFunction("get")

    val exposedColumn = ClassIds.COLUMN.symbolOrNull()

    val commonUuid = ClassIds.COMMON_UUID.classSymbol()
    val entityId = ClassIds.ENTITY_ID.symbolOrNull()
    val javaUuid = ClassIds.JAVA_UUID.symbolOrNull()
    val javaUuidType = javaUuid?.defaultType
    val javaUuidTypeN = javaUuidType?.makeNullable()

    val asCommon = CallableIds.asCommon.functions()
    val asCommonEntityId = asCommon.firstOrNull { it.owner.extensionReceiverParameter?.type?.isSubtypeOfClass(entityId !!) == true }
    val asCommonUuid = asCommon.firstOrNull { it.owner.extensionReceiverParameter?.type?.isSubtypeOfClass(javaUuid !!) == true }

    val asJavaUuid = CallableIds.asJava.functions().firstOrNull { it.owner.extensionReceiverParameter?.type?.isSubtypeOfClass(commonUuid) == true }
    val asEntityId = CallableIds.asEntityID.functions().firstOrNull {
        it.owner.extensionReceiverParameter?.type?.isSubtypeOfClass(commonUuid) == true
            && it.owner.valueParameters.firstOrNull()?.type?.isSubtypeOfClass(exposedColumn !!) == true
    }

}