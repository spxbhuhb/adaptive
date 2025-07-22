/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.kotlin.service

import `fun`.adaptive.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.Name

object Strings {
    const val SERVICES_PACKAGE = "fun.adaptive.service"
    const val SERVICES_API_PACKAGE = "fun.adaptive.service.api"
    const val SERVICES_TRANSPORT_PACKAGE = "fun.adaptive.service.transport"
    const val BACKEND_PACKAGE = "fun.adaptive.backend"
    const val BACKEND_BUILTIN_PACKAGE = "fun.adaptive.backend.builtin"

    const val SERVICE_API = "ServiceApi"
    const val SERVICE_PROVIDER = "ServiceProvider"

    const val SERVICE_CONSUMER = "ServiceConsumer"
    const val SERVICE_NAME = "serviceName"

    const val SERVICE_CALL_TRANSPORT_PROPERTY = "serviceCallTransport"
    const val WIREFORMAT_ENCODER = "wireFormatEncoder"
    const val WIREFORMAT_DECODER = "wireFormatDecoder"
    const val CALL_SERVICE = "callService"

    const val SERVICE_IMPL = "ServiceImpl"
    const val SERVICE_CONTEXT_PROPERTY = "serviceContext"
    const val NEW_INSTANCE = "newInstance"
    const val DISPATCH = "dispatch"
    const val FRAGMENT = "fragment"

    const val SERVICE_CALL_TRANSPORT = "ServiceCallTransport"

    const val SERVICE_CONTEXT = "ServiceContext"
    const val CONSUMER = "Consumer"

    const val GET_SERVICE = "getService"

    val FUN_NAMES_TO_SKIP = listOf("equals", "hashCode", "toString", CALL_SERVICE)
}

object Names {
    private fun String.name() = Name.identifier(this)

    val SERVICE_NAME = Strings.SERVICE_NAME.name()
    val CONSUMER = Strings.CONSUMER.name()
    val SERVICE_CONTEXT_PROPERTY = Strings.SERVICE_CONTEXT_PROPERTY.name()
    val SERVICE_CALL_TRANSPORT_PROPERTY = Strings.SERVICE_CALL_TRANSPORT_PROPERTY.name()
    val FRAGMENT = Strings.FRAGMENT.name()
    val NEW_INSTANCE = Strings.NEW_INSTANCE.name()
    val DISPATCH = Strings.DISPATCH.name()

    val SERVICE_CONTEXT_PARAMETER = "serviceContext".name()
    val FUN_NAME = "funName".name()
    val PAYLOAD = "payload".name()
}

object FqNames : NamesBase(Strings.SERVICES_PACKAGE) {
    val SERVICES_TRANSPORT_PACKAGE = Strings.SERVICES_TRANSPORT_PACKAGE.fqName()
    val BACKEND_BUILTIN_PACKAGE = Strings.BACKEND_BUILTIN_PACKAGE.fqName()
    val BACKEND_PACKAGE = Strings.BACKEND_PACKAGE.fqName()
    val SERVICE_API = defaultPackage.child(Strings.SERVICE_API.name())
    val SERVICE_PROVIDER = defaultPackage.child(Strings.SERVICE_PROVIDER.name())
    val WIREFORMAT_PACKAGE = "fun.adaptive.wireformat".fqName()
}

object ClassIds : NamesBase(Strings.SERVICES_PACKAGE) {
    val SERVICE_CONSUMER = Strings.SERVICE_CONSUMER.classId()
    val SERVICE_IMPL = Strings.SERVICE_IMPL.classId { FqNames.BACKEND_BUILTIN_PACKAGE }
    val SERVICE_CONTEXT = Strings.SERVICE_CONTEXT.classId()
    val SERVICE_CALL_TRANSPORT = Strings.SERVICE_CALL_TRANSPORT.classId { FqNames.SERVICES_TRANSPORT_PACKAGE }
    val BACKEND_FRAGMENT = "BackendFragment".classId { FqNames.BACKEND_PACKAGE }
    val KOTLIN_BYTEARRAY = "ByteArray".classId { "kotlin".fqName() }
    val WIREFORMAT_DECODER = "WireFormatDecoder".classId { FqNames.WIREFORMAT_PACKAGE }
}

object CallableIds : NamesBase(Strings.SERVICES_API_PACKAGE) {
    val GET_SERVICE = Strings.GET_SERVICE.callableId()
}