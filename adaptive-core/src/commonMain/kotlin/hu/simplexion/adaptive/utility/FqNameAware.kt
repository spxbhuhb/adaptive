/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.utility

interface FqNameAware {

    /**
     * Fully qualified class name of the class that implements `FqNameAware`.
     *
     * ```kotlin
     * override var classFqName
     *     get() = "<fully qualified name of the service class>"
     * ```
     */
    val classFqName: String
        get() = pluginGenerated()

}