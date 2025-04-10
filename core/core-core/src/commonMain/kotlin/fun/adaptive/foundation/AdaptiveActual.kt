/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("unused")

package `fun`.adaptive.foundation

/**
 * Marks a class as an actual fragment implementation.
 *
 * IMPORTANT `namespace` is not used at the moment because of compiler issues. See `doc/foundation/export.md` for details.
 *
 * @property  namespace  The namespace the given fragment implementation belongs to. When
 *                       `<auto>` the compiler plugin splits the name of the class by the
 *                       capital letters and uses the first part as the namespace. For example:
 *                       `SvgGroup` is turned into `svg:group`, `MyPrettyFragment` is turned into
 *                       `my:prettyfragment`.
 *
 */
@Target(AnnotationTarget.CLASS)
annotation class AdaptiveActual(
    // TODO Use AdaptiveActual.namespace to automatically build fragment factories.
    val namespace: String = "<auto>"
)