/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.wireformat

interface TestHelpers<ST> {

    fun packForTest(byteArray: ByteArray): ByteArray

    fun single(decoder: WireFormatDecoder<ST>): ST

    fun dump(byteArray: ByteArray): String

}