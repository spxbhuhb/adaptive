/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.adat

import hu.simplexion.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName

object Strings {
    const val RUNTIME_PACKAGE = "hu.simplexion.adaptive.adat"
    const val METADATA_PACKAGE = "hu.simplexion.adaptive.adat.metadata"
    const val WIREFORMAT_PACKAGE = "hu.simplexion.adaptive.adat.wireformat"
    const val ADAT_METADATA = "adatMetaData"
    const val DECODE_METADATA = "decodeMetaData"
}

object Names : NamesBase(Strings.RUNTIME_PACKAGE) {
    val ADAT_VALUES = "adatValues".name()
    val ADAT_COMPANION = "adatCompanion".name()
    val ADAT_METADATA = Strings.ADAT_METADATA.name()
    val ADAT_WIREFORMAT = "adatWireFormat".name()

    val NEW_INSTANCE = "newInstance".name()

    val ADAT_EQUALS = "adatEquals".name()
    val ADAT_HASHCODE = "adatHashCode".name()
    val ADAT_TO_STRING = "adatToString".name()

    val GET_VALUE = "getValue".name()
    val SET_VALUE = "setValue".name()

    val EQUALS = "equals".name()
    val HASHCODE = "hashCode".name()
    val TO_STRING = "toString".name()

    val OTHER = "other".name()
}

object FqNames {
    val ADAT_ANNOTATION = FqName("hu.simplexion.adaptive.adat.Adat")
}

object ClassIds : NamesBase(Strings.RUNTIME_PACKAGE) {
    val ADAT_CLASS = "AdatClass".classId()
    val ADAT_COMPANION = "AdatCompanion".classId()

    val ADAT_CLASS_METADATA = "AdatClassMetaData".classId { Strings.METADATA_PACKAGE.fqName() }
    val ADAT_CLASS_WIREFORMAT = "AdatClassWireFormat".classId { Strings.WIREFORMAT_PACKAGE.fqName() }

    val KOTLIN_ARRAY = "Array".classId { "kotlin".fqName() }
    val KOTLIN_ANY = "Any".classId { "kotlin".fqName() }

    val WIREFORMAT_REGISTRY = "WireFormatRegistry".classId { "hu.simplexion.adaptive.wireformat".fqName() }
}

object CallableIds : NamesBase(Strings.RUNTIME_PACKAGE) {

}

object Indices {

}