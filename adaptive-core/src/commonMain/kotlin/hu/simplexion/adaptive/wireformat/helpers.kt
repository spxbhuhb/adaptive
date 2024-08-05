/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

import hu.simplexion.adaptive.wireformat.json.JsonWireFormatDecoder
import hu.simplexion.adaptive.wireformat.json.JsonWireFormatEncoder

fun <T> T.toJson(wireFormat: WireFormat<T>) =
    JsonWireFormatEncoder().rawInstance(this, wireFormat).pack()

fun <T> ByteArray.fromJson(wireFormat: WireFormat<T>) =
    JsonWireFormatDecoder(this).asInstance(wireFormat)
