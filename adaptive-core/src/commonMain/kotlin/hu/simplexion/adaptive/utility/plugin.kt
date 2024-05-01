/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

/**
 * Placeholder for generated code. Throws [IllegalStateException] exception if called.
 */
fun pluginGenerated(@Suppress("UNUSED_PARAMETER") vararg args: Any?): Nothing {
    throw IllegalStateException("This code should of been replaced automatically. Is the Adaptive plugin missing?")
}

/**
 * Placeholder of optionally generated code. Throws [IllegalStateException] exception if called.
 */
fun manualOrPlugin(subject : String, @Suppress("UNUSED_PARAMETER") vararg args: Any?) : Nothing {
    throw IllegalStateException("$subject should be overridden manually or by the compiler plugin, is the plugin missing?")
}

/**
 * Placeholder of code that may be overridden, but should not be called if not overridden.
 * Throws [UnsupportedOperationException] exception if called.
 */
fun overrideManually(subject : String, @Suppress("UNUSED_PARAMETER") vararg args: Any?) : Nothing {
    throw UnsupportedOperationException("$subject should be overridden manually if you are using it")
}