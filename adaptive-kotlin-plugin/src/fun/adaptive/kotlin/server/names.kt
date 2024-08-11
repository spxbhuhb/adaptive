/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.server

import `fun`.adaptive.kotlin.common.NamesBase

object Strings {
    const val SERVER_PACKAGE = "fun.adaptive.server"
    const val SERVER_COMPONENT_PACKAGE = "fun.adaptive.server.builtin"

    const val SERVER_FRAGMENT_IMPL = "ServerFragmentImpl"
    const val ADAPTIVE_SERVER_FRAGMENT = "AdaptiveServerFragment"
    const val FRAGMENT_PROPERTY = "fragment"
    const val LOGGER_PROPERTY = "logger"

}

object Names : NamesBase(Strings.SERVER_PACKAGE) {
    val FRAGMENT_PROPERTY = Strings.FRAGMENT_PROPERTY.name()
    val LOGGER_PROPERTY = Strings.LOGGER_PROPERTY.name()

}

object ClassIds : NamesBase(Strings.SERVER_PACKAGE) {
    val SERVER_FRAGMENT_IMPL = Strings.SERVER_FRAGMENT_IMPL.classId { Strings.SERVER_COMPONENT_PACKAGE.fqName() }
    val ADAPTIVE_SERVER_FRAGMENT = Strings.ADAPTIVE_SERVER_FRAGMENT.classId()
}