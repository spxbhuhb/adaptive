/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.fqnameaware

import hu.simplexion.adaptive.kotlin.common.NamesBase

object Strings {
    const val UTILITY_PACKAGE = "hu.simplexion.adaptive.utility"
    const val FQ_NAME_AWARE = "FqNameAware"
    const val CLASS_FQ_NAME = "classFqName"
}

object Names : NamesBase(Strings.UTILITY_PACKAGE) {
    val CLASS_FQ_NAME = Strings.CLASS_FQ_NAME.name()
}

object ClassIds : NamesBase(Strings.UTILITY_PACKAGE) {
    val FQ_NAME_AWARE = Strings.FQ_NAME_AWARE.classId()
}