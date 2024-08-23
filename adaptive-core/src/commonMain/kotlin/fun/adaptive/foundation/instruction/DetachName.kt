/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.foundation.instruction

/**
 * The compiler plugin sets parameters annotated with [DetachName] to the fully
 * qualified name of the fragment function that is detached. For example:
 * `detach { hello() }` will set the parameter to `some.pkg.hello`.
 *
 * The plugin sets the value **ONLY** if the default value of the parameter is
 * used, that is, no value is passed directly in the code.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DetachName
