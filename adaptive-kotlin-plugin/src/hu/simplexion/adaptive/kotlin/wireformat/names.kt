/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.kotlin.wireformat

import hu.simplexion.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val WIREFORMAT_PACKAGE = "hu.simplexion.adaptive.wireformat"
    const val BUILTIN_PACKAGE = "hu.simplexion.adaptive.wireformat.builtin"

    const val PACK = "pack"

    const val WIREFORMAT = "WireFormat"

    const val STANDALONE = "Standalone"
    const val WIREFORMAT_ENCODER = "WireFormatEncoder"
    const val WIREFORMAT_DECODER = "WireFormatDecoder"

    const val WIREFORMAT_PROVIDER = "WireFormatProvider"
    const val DEFAULT_WIREFORMAT_PROVIDER = "defaultWireFormatProvider"

    const val ENCODE_INSTANCE = "encodeInstance"
    const val DECODE_INSTANCE = "decodeInstance"

    const val DECODE_INSTANCE_OR_NULL = "decodeInstanceOrNull"

    const val ENCODE_INSTANCE_LIST = "encodeInstanceList"
    const val DECODE_INSTANCE_LIST = "encodeInstanceList"

    const val DECODE_INSTANCE_LIST_OR_NULL = "decodeInstanceListOrNull"

    const val INSTANCE = "instance"
    const val INSTANCE_OR_NULL = "instanceOrNull"
    const val INSTANCE_LIST = "instanceList"
    const val INSTANCE_LIST_OR_NULL = "instanceListOrNull"

    const val LIST_WIREFORMAT = "$BUILTIN_PACKAGE.ListWireFormat"

    const val DURATION_WIREFORMAT = "$BUILTIN_PACKAGE.DurationWireFormat"
    const val INSTANT_WIREFORMAT = "$BUILTIN_PACKAGE.InstantWireFormat"
    const val LOCALDATE_WIREFORMAT = "$BUILTIN_PACKAGE.LocalDateWireFormat"
    const val LOCALDATETIME_WIREFORMAT = "$BUILTIN_PACKAGE.LocalDateTimeWireFormat"
    const val LOCALTIME_WIREFORMAT = "$BUILTIN_PACKAGE.LocalTimeWireFormat"
}

object FqNames {
    val WIREFORMAT_PACKAGE = FqName(Strings.WIREFORMAT_PACKAGE)
}

object ClassIds : NamesBase(Strings.WIREFORMAT_PACKAGE) {
    val WIREFORMAT = Strings.WIREFORMAT.classId()

    val STANDALONE = Strings.STANDALONE.classId()
    val WIREFORMAT_ENCODER = Strings.WIREFORMAT_ENCODER.classId()
    val WIREFORMAT_DECODER = Strings.WIREFORMAT_DECODER.classId()

    val WIREFORMAT_PROVIDER = Strings.WIREFORMAT_PROVIDER.classId()
}

object CallableIds : NamesBase(Strings.WIREFORMAT_PACKAGE) {
    val DEFAULT_WIREFORMAT_PROVIDER = Strings.DEFAULT_WIREFORMAT_PROVIDER.callableId {
        (Strings.WIREFORMAT_PACKAGE + "." + Strings.WIREFORMAT_PROVIDER + ".Companion").fqName()
    }
}
