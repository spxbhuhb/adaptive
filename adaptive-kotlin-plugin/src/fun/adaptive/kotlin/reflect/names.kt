/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.reflect

import `fun`.adaptive.kotlin.common.NamesBase

object Strings {
    const val REFLECT_PACKAGE = "fun.adaptive.reflect"
    const val UNKNOWN = "<unknown>"
}

object Names : NamesBase(Strings.REFLECT_PACKAGE) {
    val CALL_SITE_NAME_PARAMETER = "callSiteName".name()
}

object FqNames : NamesBase(Strings.REFLECT_PACKAGE) {
    val CALL_SITE_NAME_ANNOTATION = "CallSiteName".fqName { Strings.REFLECT_PACKAGE }
}