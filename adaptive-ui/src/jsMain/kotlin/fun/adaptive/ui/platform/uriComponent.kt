/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.platform

external fun decodeURIComponent(encodedURI: String): String

/**
 * This is a javascript standard function.
 * TODO IIRC there is a Ktor Client implementation for this that is MPP
 */
external fun encodeURIComponent(encodedURI: String): String