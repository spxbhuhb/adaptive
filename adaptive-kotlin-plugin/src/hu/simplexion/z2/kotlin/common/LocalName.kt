/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.z2.kotlin.common

import org.jetbrains.kotlin.name.Name

class LocalName(
    val asString : String
) {
    val asName = Name.identifier(asString)

}