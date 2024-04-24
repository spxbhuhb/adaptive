/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.common

import hu.simplexion.z2.kotlin.adaptive.ClassIds.name
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

const val UTIL_PACKAGE = "hu.simplexion.z2.util"
const val UUID = "UUID"

const val KOTLIN = "kotlin"
const val KOTLIN_COLLECTIONS = "kotlin.collections"
const val LIST = "List"
const val NOT_IMPLEMENTED_ERROR = "NotImplementedError"

val FqName.companionClassId
    get() = ClassId(parent(), shortName()).createNestedClassId(CommonNames.COMPANION_OBJECT)

object CommonStrings {
    const val COMPANION_OBJECT = "Companion"
}

object CommonNames : NamesBase("") {
    val COMPANION_OBJECT = CommonStrings.COMPANION_OBJECT.name()
}

open class NamesBase(
    defaultPackage: String
) {
    val defaultPackage = FqName(defaultPackage)

    protected fun String.name() = Name.identifier(this)

    protected fun String.fqName() = FqName(this)

    protected fun String.classId(packageFun: () -> FqName = { defaultPackage }) =
        ClassId(packageFun(), Name.identifier(this))

    protected fun String.callableId(packageFun: () -> FqName = { defaultPackage }) =
        CallableId(packageFun(), Name.identifier(this))
}