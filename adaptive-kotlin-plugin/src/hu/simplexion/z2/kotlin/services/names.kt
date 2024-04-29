/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services

import hu.simplexion.z2.kotlin.common.NamesBase
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object Strings {
    const val SERVICES_PACKAGE = "hu.simplexion.z2.services"
    const val SERVICES_TRANSPORT_PACKAGE = "hu.simplexion.z2.services.transport"

    const val SERVICE = "Service"
    const val FQ_NAME = "fqName"

    const val SERVICE_CALL_TRANSPORT_PROPERTY = "serviceCallTransport"
    const val WIREFORMAT_ENCODER = "wireFormatEncoder"
    const val WIREFORMAT_DECODER = "wireFormatDecoder"
    const val CALL_SERVICE = "callService"

    const val SERVICE_IMPL = "ServiceImpl"
    const val SERVICE_CONTEXT_PROPERTY = "serviceContext"
    const val NEW_INSTANCE = "newInstance"
    const val DISPATCH = "dispatch"

    const val SERVICE_CALL_TRANSPORT = "ServiceCallTransport"

    const val SERVICE_CONTEXT = "ServiceContext"
    const val CONSUMER = "Consumer"

    const val GET_SERVICE = "getService"
    const val DEFAULT_SERVICE_CALL_TRANSPORT = "defaultServiceCallTransport"

    val FUN_NAMES_TO_SKIP = listOf("equals", "hashCode", "toString", CALL_SERVICE)
}

object Names {
    private fun String.name() = Name.identifier(this)

    val FQ_NAME = Strings.FQ_NAME.name()
    val CONSUMER = Strings.CONSUMER.name()
    val SERVICE_CONTEXT_PROPERTY = Strings.SERVICE_CONTEXT_PROPERTY.name()
    val SERVICE_CALL_TRANSPORT_PROPERTY = Strings.SERVICE_CALL_TRANSPORT_PROPERTY.name()
}

object FqNames : NamesBase(Strings.SERVICES_PACKAGE) {
    val SERVICES_TRANSPORT_PACKAGE = FqName.fromSegments(Strings.SERVICES_TRANSPORT_PACKAGE.split('.'))
    val SERVICE_IMPL = Strings.SERVICE_IMPL.fqName()
}

object ClassIds : NamesBase(Strings.SERVICES_PACKAGE) {
    val SERVICE = Strings.SERVICE.classId()
    val SERVICE_IMPL = Strings.SERVICE_IMPL.classId()
    val SERVICE_CONTEXT = Strings.SERVICE_CONTEXT.classId()
    val SERVICE_CALL_TRANSPORT = Strings.SERVICE_CALL_TRANSPORT.classId { FqNames.SERVICES_TRANSPORT_PACKAGE }
}

object CallableIds : NamesBase(Strings.SERVICES_PACKAGE) {
    val GET_SERVICE = Strings.GET_SERVICE.callableId()
    val DEFAULT_SERVICE_CALL_TRANSPORT = Strings.DEFAULT_SERVICE_CALL_TRANSPORT.callableId()
}

object Indices {
    const val DISPATCH_FUN_NAME = 0
    const val DISPATCH_PAYLOAD = 1

    const val CALL_SERVICE_NAME = 0
    const val CALL_FUN_NAME = 1
    const val CALL_PAYLOAD = 2
}
