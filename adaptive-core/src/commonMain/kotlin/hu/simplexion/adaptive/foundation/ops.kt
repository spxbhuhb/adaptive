/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.foundation

/**
 * The document root for [ops].
 */
var opsDocRoot = "https://github.com/spxbhuhb/adaptive/blob/main/doc/problems"

/**
 * Throws an exception that points to an url under [opsDocRoot], specified by [docPath].
 *
 * [message]
 * - [trimIndent]
 * - replace all tabs and newlines with spaces
 *
 * [docPath]
 * - convert space to %20, other special characters are included as-is (probably breaks the URL).
 * - add ".md" if not present
 *
 * ```kotlin
 * ops(
 *     "addActual",
 *     """
 *         Problem description
 *     """,
 * )
 * ```
 *
 * Results in:
 *
 * ```text
 * Problem description. For more information see: https://github.com/spxbhuhb/adaptive/blob/main/doc/internals/Actual%20UI.md
 * ```
 */
fun ops(docPath: String, message: String): Nothing {
    val fullPath = "$opsDocRoot/${if (docPath.startsWith("/")) docPath.substring(1) else docPath}"
    val escapedPath = fullPath.replace(" ", "%20")
    val mdPath = if (escapedPath.endsWith(".md")) escapedPath else "$escapedPath.md"

    val trimmedMessage = message.trimIndent().replace("\n", " ").replace("\r", " ").replace("\t", " ")

    throw IllegalStateException("$trimmedMessage (For more information see: $mdPath)")
}

/**
 * Call to this function indicates that the code will be generated by the compiler plugin.
 */
fun replacedByPlugin(message: String): Nothing {
    ops(
        "replacedByPlugin",
        """
            the compiler plugin should replace this code,
            maybe Adaptive plugin is missing from your gradle configuration,
            if not, please open a bug report on GitHub,
            message: $message")
        """
    )
}

/**
 * Call to this function indicates that the adaptive class of this fragment is written
 * manually. This is typical for bridge-dependent classes that interact directly with
 * the underlying UI. Most probably you'll find the class right under the function.
 */
fun manualImplementation(vararg arguments: Any?) {
    ops(
        "manualImplementation",
        """
            manual implementation function should never be called,
            maybe Adaptive plugin is missing from your gradle configuration,
            arguments: ${arguments.contentToString()}")
        """
    )
}