/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.WireFormatProvider.Companion.defaultWireFormatProvider
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatProvider
import hu.simplexion.adaptive.wireformat.protobuf.ProtoWireFormatProvider

fun withJson() {
    defaultWireFormatProvider = JsonWireFormatProvider()
}

fun withProtobuf() {
    defaultWireFormatProvider = ProtoWireFormatProvider()
}