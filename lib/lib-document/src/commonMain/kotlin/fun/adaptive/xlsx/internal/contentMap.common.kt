/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.xlsx.internal

import `fun`.adaptive.xlsx.model.XlsxDocument


internal typealias Appender = (String) -> Unit

/**
 * Path and file content pairs for zip packaging.
 */
internal typealias ContentMap = HashMap<String, (Appender) -> Unit>

/**
 * Compress data and pack it into a byte array.
 */
internal expect suspend fun ContentMap.pack() : ByteArray