/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.kotlin.server

import hu.simplexion.adaptive.kotlin.common.NamesBase

object Strings {
    const val SERVER_PACKAGE = "hu.simplexion.adaptive.server"
    const val SERVER_COMPONENT_PACKAGE = "hu.simplexion.adaptive.server.component"

    const val SERVER_FRAGMENT_IMPL = "ServerFragmentImpl"
    const val ADAPTIVE_SERVER_ADAPTER = "AdaptiveServerAdapter"
    const val SERVER_ADAPTER_PROPERTY = "serverAdapter"
}

object Names : NamesBase(Strings.SERVER_PACKAGE) {
    val SERVER_ADAPTER_PROPERTY = Strings.SERVER_ADAPTER_PROPERTY.name()
}

object ClassIds : NamesBase(Strings.SERVER_PACKAGE) {
    val SERVER_FRAGMENT_IMPL = Strings.SERVER_FRAGMENT_IMPL.classId { Strings.SERVER_COMPONENT_PACKAGE.fqName() }
    val ADAPTIVE_SERVER_ADAPTER = Strings.ADAPTIVE_SERVER_ADAPTER.classId()
}