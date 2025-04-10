/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.backend

import `fun`.adaptive.kotlin.common.NamesBase

object Strings {
    const val BACKEND_PACKAGE = "fun.adaptive.backend"
    const val BACKEND_BUILTIN_PACKAGE = "fun.adaptive.backend.builtin"

    const val BACKEND_FRAGMENT_IMPL = "BackendFragmentImpl"
    const val BACKEND_FRAGMENT = "BackendFragment"
    const val FRAGMENT_PROPERTY = "fragment"
    const val LOGGER_PROPERTY = "logger"

}

object Names : NamesBase(Strings.BACKEND_PACKAGE) {
    val FRAGMENT_PROPERTY = Strings.FRAGMENT_PROPERTY.name()
    val LOGGER_PROPERTY = Strings.LOGGER_PROPERTY.name()
}

object ClassIds : NamesBase(Strings.BACKEND_PACKAGE) {
    val BACKEND_FRAGMENT_IMPL = Strings.BACKEND_FRAGMENT_IMPL.classId { Strings.BACKEND_BUILTIN_PACKAGE.fqName() }
    val BACKEND_FRAGMENT = Strings.BACKEND_FRAGMENT.classId()
}