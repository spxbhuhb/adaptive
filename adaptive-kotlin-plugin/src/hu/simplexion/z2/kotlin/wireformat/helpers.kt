/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.kotlin.wireformat

import hu.simplexion.z2.kotlin.common.asClassId
import hu.simplexion.z2.kotlin.common.functionByName
import org.jetbrains.kotlin.ir.util.isSubclassOf
import org.jetbrains.kotlin.name.ClassId

fun String.toWireFormatType(pluginContext: WireFormatPluginContext, signature: String): WireFormatType {
    return with(pluginContext) {
        val type = this@toWireFormatType
        val lcType = type.take(1).lowercase() + type.drop(1)
        val ucType = type.take(1).uppercase() + type.drop(1)

        val className = Strings.BUILTIN_PACKAGE + "." + ucType + "WireFormat"
        val classId = className.asClassId

        val symbol = checkNotNull(pluginContext.irContext.referenceClass(classId)) { "missing class: ${classId.asFqNameString()}" }
        check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${classId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

        WireFormatType(
            encode = wireFormatEncoder.functionByName { lcType }, // boolean
            decode = wireFormatDecoder.functionByName { lcType }, // boolean

            encodeOrNull = wireFormatEncoder.functionByName { "${lcType}OrNull" }, // booleanOrNull
            decodeOrNull = wireFormatDecoder.functionByName { "${lcType}OrNull" }, // booleanOrNull

            primitive = true,
            signature = signature,
            wireFormat = symbol
        )
    }
}

fun String.toInstanceWireFormatType(pluginContext: WireFormatPluginContext): WireFormatType =
    this.asClassId.toInstanceWireFormatType(pluginContext)

fun ClassId.toInstanceWireFormatType(pluginContext: WireFormatPluginContext): WireFormatType {
    val classId = this

    val symbol = checkNotNull(pluginContext.irContext.referenceClass(classId)) { "missing class: ${classId.asFqNameString()}" }
    check(symbol.owner.isSubclassOf(pluginContext.wireFormatClass.owner)) { "class ${classId.asFqNameString()} does not implement ${ClassIds.WIREFORMAT}" }

    return WireFormatType(
        encode = pluginContext.encodeInstance,
        decode = pluginContext.decodeInstance,

        encodeOrNull = pluginContext.encodeInstanceOrNull,
        decodeOrNull = pluginContext.decodeInstanceOrNull,

        primitive = false,
        signature = "L${classId.asFqNameString()};",
        wireFormat = symbol
    )
}
