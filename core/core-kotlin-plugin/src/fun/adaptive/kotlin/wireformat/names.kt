/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.kotlin.wireformat

import `fun`.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val WIREFORMAT_PACKAGE = "fun.adaptive.wireformat"
    const val BUILTIN_PACKAGE = "fun.adaptive.wireformat.builtin"
    const val SIGNATURE_PACKAGE = "fun.adaptive.wireformat.signature"
    const val ADAT_PACKAGE = "fun.adaptive.adat"

    const val WIREFORMAT = "WireFormat"

    const val WIREFORMAT_ENCODER = "WireFormatEncoder"
    const val WIREFORMAT_DECODER = "WireFormatDecoder"

    const val INSTANCE = "instance"
    const val INSTANCE_OR_NULL = "instanceOrNull"

    const val PSEUDO_INSTANCE_START = "pseudoInstanceStart"
    const val PSEUDO_INSTANCE_END = "pseudoInstanceEnd"

    const val WIREFORMAT_TYPE_ARGUMENT = "WireFormatTypeArgument"

    // issue #33 const val ARRAY_WIREFORMAT = "$BUILTIN_PACKAGE.ArrayWireFormat"
    const val LIST_WIREFORMAT = "$BUILTIN_PACKAGE.ListWireFormat"
    const val SET_WIREFORMAT = "$BUILTIN_PACKAGE.SetWireFormat"
    const val MAP_WIREFORMAT = "$BUILTIN_PACKAGE.MapWireFormat"
    const val PAIR_WIREFORMAT = "$BUILTIN_PACKAGE.PairWireFormat"

    const val DURATION_WIREFORMAT = "$BUILTIN_PACKAGE.DurationWireFormat"
    const val INSTANT_WIREFORMAT = "$BUILTIN_PACKAGE.InstantWireFormat"
    const val LOCALDATE_WIREFORMAT = "$BUILTIN_PACKAGE.LocalDateWireFormat"
    const val LOCALDATETIME_WIREFORMAT = "$BUILTIN_PACKAGE.LocalDateTimeWireFormat"
    const val LOCALTIME_WIREFORMAT = "$BUILTIN_PACKAGE.LocalTimeWireFormat"
}

object FqNames {
    val SIGNATURE_PACKAGE = FqName(Strings.SIGNATURE_PACKAGE)
    val BUILTIN_PACKAGE = FqName(Strings.BUILTIN_PACKAGE)
    val ADAT_PACKAGE = FqName(Strings.ADAT_PACKAGE)
}

object ClassIds : NamesBase(Strings.WIREFORMAT_PACKAGE) {
    val WIREFORMAT = Strings.WIREFORMAT.classId()
    val ADATCLASS = "AdatClass".classId { FqNames.ADAT_PACKAGE }

    val WIREFORMAT_ENCODER = Strings.WIREFORMAT_ENCODER.classId()
    val WIREFORMAT_DECODER = Strings.WIREFORMAT_DECODER.classId()

    val WIREFORMAT_TYPE_ARGUMENT = Strings.WIREFORMAT_TYPE_ARGUMENT.classId { FqNames.SIGNATURE_PACKAGE }

    val POLYMORPHIC_WIREFORMAT = "PolymorphicWireFormat".classId { FqNames.BUILTIN_PACKAGE }
}