/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

import kotlinx.cinterop.*
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault

@OptIn(ExperimentalForeignApi::class)
actual fun fourRandomInt(): IntArray {
    val buffer = IntArray(4)
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