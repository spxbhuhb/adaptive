/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat.ir

import hu.simplexion.adaptive.kotlin.AdaptiveOptions
import hu.simplexion.adaptive.kotlin.adat.CallableIds
import hu.simplexion.adaptive.kotlin.adat.ClassIds
import hu.simplexion.adaptive.kotlin.common.AbstractPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
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

    val commonUuidType = ClassIds.COMMON_UUID.classSymbol()
    val entityIdType = ClassIds.ENTITY_ID.symbolOrNull()
    val javaUuidType = ClassIds.JAVA_UUID.symbolOrNull()

    val asCommon = CallableIds.asCommon.functions()
    val asCommonEntityId = asCommon.firstOrNull { it.owner.extensionReceiverParameter?.type == entityIdType }
    val asCommonUuid = asCommon.firstOrNull { it.owner.extensionReceiverParameter?.type == javaUuidType }
    val asJavaUuid = CallableIds.asJvm.functions().first { it.owner.extensionReceiverParameter?.type == commonUuidType }

}