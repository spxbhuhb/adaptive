/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.backend

import `fun`.adaptive.kotlin.common.NamesBase
import `fun`.adaptive.kotlin.service.FqNames

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

object FqNames : NamesBase(Strings.BACKEND_PACKAGE) {
    val LOG_PACKAGE = "fun.adaptive.log".fqName()
}

object ClassIds : NamesBase(Strings.BACKEND_PACKAGE) {
    val WORKER_IMPL = "WorkerImpl".classId { Strings.BACKEND_BUILTIN_PACKAGE.fqName() }
    val BACKEND_FRAGMENT_IMPL = Strings.BACKEND_FRAGMENT_IMPL.classId { Strings.BACKEND_BUILTIN_PACKAGE.fqName() }
    val BACKEND_FRAGMENT = Strings.BACKEND_FRAGMENT.classId()
}

object CallableIds : NamesBase(`fun`.adaptive.kotlin.service.Strings.SERVICES_API_PACKAGE) {
    val GET_LOGGER = "getLogger".callableId { FqNames.LOG_PACKAGE }
}