/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.utility


actual fun secureRandom(count : Int): IntArray {
    val buffer = IntArray(count)

    // TODO this works properly in browser only, in tests / node should use crypto module
    js(
        """
        if (window && window.crypto) {
            window.crypto.getRandomValues(buffer);
        } else {
            for (var i = 0; i < buffer.length; i++) {
                buffer[i] = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER);
            }
        }
         """
    )

    return buffer
}