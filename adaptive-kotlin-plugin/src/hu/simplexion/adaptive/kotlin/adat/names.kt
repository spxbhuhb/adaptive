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

    const val EXPOSED_PACKAGE = "hu.simplexion.adaptive.exposed"
    const val JETBRAINS_EXPOSED_PACKAGE = "org.jetbrains.exposed.sql"
    const val RESULT_ROW = "ResultRow"
    const val COLUMN = "Column"
    const val UPDATE_STATEMENT = "UpdateStatement"
    const val EXPOSED_ADAT_TABLE = "ExposedAdatTable"
    const val EXPOSED_ADAT_SET = "ExposedAdatSet"
    const val FROM_ROW = "fromRow"
    const val TO_ROW = "toRow"
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
    val SET = "set".name()
    val ID = "id".name()

    val FROM_ROW = Strings.FROM_ROW.name()
    val TO_ROW = Strings.TO_ROW.name()
}

object FqNames : NamesBase(Strings.RUNTIME_PACKAGE) {
    val ADAT_ANNOTATION = FqName("hu.simplexion.adaptive.adat.Adat")
    val EXPOSED_ADAT_SET = Strings.EXPOSED_ADAT_SET.fqName { Strings.EXPOSED_PACKAGE }
}

object ClassIds : NamesBase(Strings.RUNTIME_PACKAGE) {
    val ADAT_CLASS = "AdatClass".classId()
    val ADAT_COMPANION = "AdatCompanion".classId()

    val ADAT_CLASS_METADATA = "AdatClassMetaData".classId { Strings.METADATA_PACKAGE.fqName() }
    val ADAT_CLASS_WIREFORMAT = "AdatClassWireFormat".classId { Strings.WIREFORMAT_PACKAGE.fqName() }

    val KOTLIN_ARRAY = "Array".classId { "kotlin".fqName() }
    val KOTLIN_ANY = "Any".classId { "kotlin".fqName() }

    val WIREFORMAT_REGISTRY = "WireFormatRegistry".classId { "hu.simplexion.adaptive.wireformat".fqName() }

    val EXPOSED_ADAT_TABLE = Strings.EXPOSED_ADAT_TABLE.classId { Strings.EXPOSED_PACKAGE.fqName() }

    val RESULT_ROW = Strings.RESULT_ROW.classId { Strings.JETBRAINS_EXPOSED_PACKAGE.fqName() }
    val COLUMN = Strings.COLUMN.classId { Strings.JETBRAINS_EXPOSED_PACKAGE.fqName() }
    val UPDATE_STATEMENT = Strings.UPDATE_STATEMENT.classId { Strings.JETBRAINS_EXPOSED_PACKAGE.fqName() }

    val COMMON_UUID = "UUID".classId { "hu.simplexion.adaptive.utility".fqName() }
    val JAVA_UUID = "UUID".classId { "java.util".fqName() }
    val ENTITY_ID = "EntityID".classId { "org.jetbrains.exposed.dao.id".fqName() }
}

object CallableIds : NamesBase(Strings.RUNTIME_PACKAGE) {
    val exposed = Strings.EXPOSED_PACKAGE.fqName()
    val asCommon = "asCommon".callableId { exposed }
    val asEntityID = "asEntityID".callableId { exposed }
    val asJava = "asJava".callableId { exposed }
}

object Indices {

}