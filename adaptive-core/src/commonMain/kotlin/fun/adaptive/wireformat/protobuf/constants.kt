/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.wireformat.protobuf

internal const val VARINT = 0
internal const val I64 = 1
internal const val LEN = 2
internal const val I32 = 5

internal const val continuation = 0x80UL
internal const val valueMask = 0x7fUL

const val NULL_SHIFT = 20000