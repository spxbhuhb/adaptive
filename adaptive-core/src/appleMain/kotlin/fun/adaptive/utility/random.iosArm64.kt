/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.utility

import kotlinx.cinterop.*
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
actual fun secureRandom(count : Int): IntArray {
    val buffer = IntArray(2)
    buffer.usePinned { pinned ->
        val rc = SecRandomCopyBytes(
            kSecRandomDefault,
            buffer.size.convert(),
            pinned.addressOf(0)
        )
        check(rc == errSecSuccess)
    }
    return buffer
}