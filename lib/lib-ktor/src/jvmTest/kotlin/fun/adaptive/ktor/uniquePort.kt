package `fun`.adaptive.ktor

import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

private val lock = getLock()
private var nextPort = 12345

val testPort
    get() = lock.use { nextPort ++ }

