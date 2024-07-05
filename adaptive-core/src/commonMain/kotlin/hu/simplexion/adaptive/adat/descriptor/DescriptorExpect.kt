/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.adat.descriptor

@Target(AnnotationTarget.FUNCTION)
annotation class DescriptorExpect(
    val namespace: String
)